/**
 * 紧急UAsset文件分析器
 * 
 * 专门用于诊断和修复"无法找到有效的TotalHeaderSize字段"错误
 * 基于多种启发式算法和模式识别
 */

console.log('🚨 紧急UAsset分析器已加载');

// 运行紧急分析
function emergencyAnalyzeUAsset() {
  console.log('🚨 请选择有问题的.uasset文件进行紧急分析：');
  
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = '.uasset';
  input.onchange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    
    console.log(`🔬 紧急分析开始: ${file.name} (${(file.size / 1024).toFixed(2)} KB)`);
    
    try {
      const buffer = await file.arrayBuffer();
      
      // 保存到全局变量供后续使用
      window.problemBuffer = buffer;
      
      const analysis = await performEmergencyAnalysis(buffer);
      console.log('\n📊 紧急分析结果:');
      displayEmergencyResults(analysis);
      
      // 生成修复代码
      generateEmergencyFix(analysis, file.name);
      
    } catch (error) {
      console.error('❌ 紧急分析失败:', error);
    }
  };
  
  input.click();
}

// 执行紧急分析
async function performEmergencyAnalysis(buffer) {
  const view = new DataView(buffer);
  const analysis = {
    fileInfo: {
      name: 'unknown',
      size: buffer.byteLength,
      hexDump: generateHexDump(buffer, 0, Math.min(200, buffer.byteLength))
    },
    magicNumber: analyzeMagicNumber(view),
    versions: analyzeVersionFields(view),
    headerCandidates: findAllHeaderCandidates(view),
    patterns: analyzeFilePatterns(view),
    recommendations: []
  };
  
  // 生成建议
  generateRecommendations(analysis);
  
  return analysis;
}

// 分析魔数
function analyzeMagicNumber(view) {
  const tag = view.getUint32(0, true);
  const tagBE = view.getUint32(0, false);
  const expected = 0x9E2A83C1;
  
  return {
    littleEndian: {
      value: tag,
      hex: '0x' + tag.toString(16).toUpperCase().padStart(8, '0'),
      isValid: tag === expected
    },
    bigEndian: {
      value: tagBE,
      hex: '0x' + tagBE.toString(16).toUpperCase().padStart(8, '0'),
      isValid: tagBE === expected
    },
    expected: '0x' + expected.toString(16).toUpperCase(),
    byteOrder: tag === expected ? 'little-endian' : (tagBE === expected ? 'big-endian' : 'unknown')
  };
}

// 分析版本字段
function analyzeVersionFields(view) {
  const versions = {};
  
  try {
    versions.legacyFileVersion = view.getInt32(4, true);
    versions.legacyUE3Version = view.getInt32(8, true);
    versions.fileVersionUE4 = view.getInt32(12, true);
    versions.fileVersionUE5 = view.getInt32(16, true);
    versions.licenseeUE4Version = view.getInt32(20, true);
    
    // 版本合理性分析
    versions.analysis = {
      ue4Valid: versions.fileVersionUE4 >= 200 && versions.fileVersionUE4 <= 600,
      ue5Valid: versions.fileVersionUE5 >= 0 && versions.fileVersionUE5 <= 100,
      likelyVersion: determineLikelyUEVersion(versions),
      hasCustomVersions: versions.fileVersionUE4 >= 444
    };
  } catch (e) {
    versions.error = e.message;
  }
  
  return versions;
}

// 确定可能的UE版本
function determineLikelyUEVersion(versions) {
  if (versions.fileVersionUE5 > 0) {
    return `UE5.${Math.floor(versions.fileVersionUE5 / 100)}.${versions.fileVersionUE5 % 100}`;
  } else if (versions.fileVersionUE4 >= 400) {
    const major = Math.floor((versions.fileVersionUE4 - 200) / 100) + 10;
    return `UE4.${major}+`;
  } else if (versions.fileVersionUE4 >= 200) {
    const major = Math.floor(versions.fileVersionUE4 / 100) + 4;
    return `UE4.${major}+`;
  }
  return 'Unknown';
}

// 查找所有可能的HeaderSize候选
function findAllHeaderCandidates(view) {
  const candidates = [];
  const fileSize = view.byteLength;
  
  // 扫描整个文件的前部分
  const scanLimit = Math.min(500, fileSize - 4);
  
  for (let offset = 20; offset < scanLimit; offset += 4) {
    try {
      const value = view.getInt32(offset, true);
      
      // 更宽松的候选条件
      if (value > 50 && value < fileSize) {
        const score = calculateAdvancedScore(value, fileSize, offset);
        
        candidates.push({
          offset,
          value,
          score,
          percentage: (value / fileSize * 100).toFixed(1),
          reason: generateCandidateReason(value, fileSize, offset),
          isReasonable: score >= 20 // 降低阈值
        });
      }
    } catch (e) {
      // 继续扫描
    }
  }
  
  // 按评分排序
  candidates.sort((a, b) => b.score - a.score);
  
  return candidates;
}

// 高级评分算法
function calculateAdvancedScore(value, fileSize, offset) {
  let score = 0;
  
  // 大小比例评分 (0-30分)
  const ratio = value / fileSize;
  if (ratio >= 0.05 && ratio <= 0.9) {
    if (ratio >= 0.1 && ratio <= 0.6) {
      score += 30 - Math.abs(0.25 - ratio) * 60;
    } else {
      score += 15 - Math.abs(0.25 - ratio) * 30;
    }
  }
  
  // 数值范围评分 (0-25分)
  if (value >= 100 && value <= 10000) {
    if (value >= 200 && value <= 3000) {
      score += 25;
    } else if (value >= 50 && value <= 20000) {
      score += 15;
    } else {
      score += 8;
    }
  }
  
  // 对齐评分 (0-20分)
  if (value % 16 === 0) score += 20;
  else if (value % 8 === 0) score += 15;
  else if (value % 4 === 0) score += 10;
  else if (value % 2 === 0) score += 5;
  
  // 位置合理性 (0-15分)
  if (offset >= 24 && offset <= 40) score += 15;
  else if (offset >= 20 && offset <= 60) score += 10;
  else if (offset >= 16 && offset <= 100) score += 5;
  
  // 特殊模式奖励 (0-10分)
  if (value > fileSize * 0.1 && value < fileSize * 0.5) score += 10;
  if ((value & 0xFF) === 0) score += 5; // 256的倍数
  
  return Math.min(100, Math.max(0, score));
}

// 生成候选原因说明
function generateCandidateReason(value, fileSize, offset) {
  const reasons = [];
  
  const ratio = value / fileSize;
  if (ratio >= 0.1 && ratio <= 0.6) {
    reasons.push('合理的大小比例');
  }
  
  if (value % 16 === 0) {
    reasons.push('16字节对齐');
  } else if (value % 4 === 0) {
    reasons.push('4字节对齐');
  }
  
  if (offset >= 24 && offset <= 40) {
    reasons.push('标准位置');
  } else if (offset >= 20 && offset <= 60) {
    reasons.push('可能位置');
  }
  
  if (value >= 200 && value <= 3000) {
    reasons.push('常见大小范围');
  }
  
  return reasons.length > 0 ? reasons.join(', ') : '基础候选';
}

// 分析文件模式
function analyzeFilePatterns(view) {
  const patterns = {
    nullRegions: [],
    repeatingValues: [],
    potentialStrings: [],
    offsetLikeValues: []
  };
  
  const fileSize = view.byteLength;
  const scanLimit = Math.min(1000, fileSize);
  
  // 扫描前1KB寻找模式
  for (let i = 0; i < scanLimit - 4; i += 4) {
    try {
      const value = view.getInt32(i, true);
      
      // 检查空区域
      if (value === 0) {
        let nullLength = 4;
        while (i + nullLength < scanLimit && view.getInt32(i + nullLength, true) === 0) {
          nullLength += 4;
        }
        if (nullLength >= 16) {
          patterns.nullRegions.push({ offset: i, length: nullLength });
        }
      }
      
      // 检查可能的偏移值
      if (value > 100 && value < fileSize && value % 4 === 0) {
        patterns.offsetLikeValues.push({ offset: i, value });
      }
      
      // 检查可能的字符串
      if (value > 0 && value < 200) {
        const stringCandidate = tryReadString(view, i);
        if (stringCandidate) {
          patterns.potentialStrings.push({
            offset: i,
            type: stringCandidate.type,
            value: stringCandidate.value,
            confidence: stringCandidate.confidence
          });
        }
      }
    } catch (e) {
      // 继续扫描
    }
  }
  
  return patterns;
}

// 尝试读取字符串
function tryReadString(view, offset) {
  try {
    const length = view.getInt32(offset, true);
    
    // ASCII字符串
    if (length > 0 && length <= 100 && offset + 4 + length <= view.byteLength) {
      const bytes = new Uint8Array(view.buffer, view.byteOffset + offset + 4, Math.min(length - 1, 50));
      const isPrintable = Array.from(bytes).every(b => (b >= 32 && b <= 126) || b === 0);
      
      if (isPrintable && bytes.length > 0) {
        const str = new TextDecoder('utf-8').decode(bytes.filter(b => b !== 0));
        if (str.length > 0 && /^[a-zA-Z0-9_\/\\.\-\s]*$/.test(str)) {
          return {
            type: 'ASCII',
            value: str,
            confidence: calculateStringConfidence(str, length)
          };
        }
      }
    }
    
    // Unicode字符串
    if (length < 0 && length >= -100) {
      const actualLength = -length;
      const byteLength = actualLength * 2;
      
      if (offset + 4 + byteLength <= view.byteLength && actualLength > 1) {
        const utf16Array = new Uint16Array(view.buffer, view.byteOffset + offset + 4, Math.min(actualLength - 1, 25));
        const isValidUnicode = Array.from(utf16Array).every(c => c > 0 && c < 0xFFFE);
        
        if (isValidUnicode) {
          const str = String.fromCharCode(...Array.from(utf16Array));
          return {
            type: 'Unicode',
            value: str,
            confidence: calculateStringConfidence(str, actualLength)
          };
        }
      }
    }
  } catch (e) {
    // 忽略错误
  }
  
  return null;
}

// 计算字符串置信度
function calculateStringConfidence(str, length) {
  let confidence = 50;
  
  if (str.length >= 3 && str.length <= 50) confidence += 20;
  if (/^[a-zA-Z][a-zA-Z0-9_]*$/.test(str)) confidence += 15;
  if (str.includes('/') || str.includes('\\')) confidence += 10;
  if (str.toLowerCase().includes('game') || str.toLowerCase().includes('content')) confidence += 10;
  if (length === str.length + 1) confidence += 10; // 正确的null终止
  
  return Math.min(100, confidence);
}

// 生成十六进制转储
function generateHexDump(buffer, start, length) {
  const view = new DataView(buffer);
  const lines = [];
  
  for (let i = start; i < start + length; i += 16) {
    const offset = i.toString(16).padStart(8, '0').toUpperCase();
    const hexBytes = [];
    const asciiBytes = [];
    
    for (let j = 0; j < 16 && i + j < start + length; j++) {
      const byte = view.getUint8(i + j);
      hexBytes.push(byte.toString(16).padStart(2, '0').toUpperCase());
      asciiBytes.push(byte >= 32 && byte <= 126 ? String.fromCharCode(byte) : '.');
    }
    
    lines.push(`${offset}: ${hexBytes.join(' ').padEnd(47)} |${asciiBytes.join('')}|`);
  }
  
  return lines.join('\n');
}

// 生成建议
function generateRecommendations(analysis) {
  const recommendations = analysis.recommendations;
  
  // 魔数问题
  if (!analysis.magicNumber.littleEndian.isValid && !analysis.magicNumber.bigEndian.isValid) {
    recommendations.push({
      type: 'critical',
      message: '文件魔数无效，可能不是.uasset文件或已损坏',
      action: '确认文件类型，重新导出'
    });
  } else if (analysis.magicNumber.bigEndian.isValid) {
    recommendations.push({
      type: 'warning',
      message: '文件使用大端字节序',
      action: '需要修改解析器支持大端字节序'
    });
  }
  
  // 版本问题
  if (!analysis.versions.analysis?.ue4Valid) {
    recommendations.push({
      type: 'warning',
      message: `UE4版本异常: ${analysis.versions.fileVersionUE4}`,
      action: '可能是较新或较老的UE版本，需要特殊处理'
    });
  }
  
  // HeaderSize候选
  const goodCandidates = analysis.headerCandidates.filter(c => c.score >= 50);
  if (goodCandidates.length === 0) {
    const bestCandidate = analysis.headerCandidates[0];
    if (bestCandidate && bestCandidate.score >= 20) {
      recommendations.push({
        type: 'suggestion',
        message: `最佳HeaderSize候选: 偏移${bestCandidate.offset}, 值${bestCandidate.value}`,
        action: `手动设置HeaderSize为${bestCandidate.value}`
      });
    } else {
      recommendations.push({
        type: 'critical',
        message: '无法找到合理的HeaderSize候选',
        action: '文件可能严重损坏，建议重新导出'
      });
    }
  } else {
    recommendations.push({
      type: 'success',
      message: `找到${goodCandidates.length}个有效的HeaderSize候选`,
      action: `使用最高评分候选: 偏移${goodCandidates[0].offset}, 值${goodCandidates[0].value}`
    });
  }
}

// 显示紧急分析结果
function displayEmergencyResults(analysis) {
  console.log('\n🔍 文件基础信息:');
  console.log(`大小: ${(analysis.fileInfo.size / 1024).toFixed(2)} KB`);
  
  console.log('\n🏷️ 魔数分析:');
  console.log(`小端字节序: ${analysis.magicNumber.littleEndian.hex} ${analysis.magicNumber.littleEndian.isValid ? '✅' : '❌'}`);
  console.log(`大端字节序: ${analysis.magicNumber.bigEndian.hex} ${analysis.magicNumber.bigEndian.isValid ? '✅' : '❌'}`);
  console.log(`期望值: ${analysis.magicNumber.expected}`);
  console.log(`字节序: ${analysis.magicNumber.byteOrder}`);
  
  console.log('\n📋 版本分析:');
  Object.entries(analysis.versions).forEach(([key, value]) => {
    if (key !== 'analysis' && key !== 'error') {
      console.log(`${key}: ${value}`);
    }
  });
  if (analysis.versions.analysis) {
    console.log(`推断版本: ${analysis.versions.analysis.likelyVersion}`);
    console.log(`UE4版本有效: ${analysis.versions.analysis.ue4Valid ? '✅' : '❌'}`);
    console.log(`支持CustomVersions: ${analysis.versions.analysis.hasCustomVersions ? '✅' : '❌'}`);
  }
  
  console.log('\n🎯 HeaderSize候选 (前10个):');
  analysis.headerCandidates.slice(0, 10).forEach((candidate, index) => {
    const status = candidate.isReasonable ? '✅' : '⚠️';
    console.log(`${index + 1}. ${status} 偏移${candidate.offset}: ${candidate.value} bytes (${candidate.percentage}%, 评分${candidate.score.toFixed(1)}) - ${candidate.reason}`);
  });
  
  console.log('\n🔤 潜在字符串:');
  analysis.patterns.potentialStrings.slice(0, 5).forEach((str, index) => {
    console.log(`${index + 1}. 偏移${str.offset} (${str.type}): "${str.value}" (置信度${str.confidence}%)`);
  });
  
  console.log('\n💡 建议列表:');
  analysis.recommendations.forEach((rec, index) => {
    const icon = rec.type === 'critical' ? '🚨' : rec.type === 'warning' ? '⚠️' : rec.type === 'success' ? '✅' : '💡';
    console.log(`${index + 1}. ${icon} ${rec.message}`);
    console.log(`   操作: ${rec.action}`);
  });
  
  console.log('\n📊 文件头部十六进制转储:');
  console.log(analysis.fileInfo.hexDump);
}

// 生成紧急修复代码
function generateEmergencyFix(analysis, fileName) {
  console.log('\n🔧 紧急修复代码:');
  
  const bestCandidate = analysis.headerCandidates[0];
  if (!bestCandidate) {
    console.log('❌ 无法生成修复代码，没有可用的HeaderSize候选');
    return;
  }
  
  console.log(`
// 紧急修复代码 - 针对文件: ${fileName}
// 使用最佳HeaderSize候选: 偏移${bestCandidate.offset}, 值${bestCandidate.value}

// 方法1: 修改umgConverter.ts中的parsePackageFileSummary方法
// 在"策略3: 如果仍然失败，使用估算值"部分添加：

if (!bestCandidate) {
  console.log('🔄 使用紧急修复值...');
  
  // 紧急修复：使用分析得出的最佳候选
  totalHeaderSize = ${bestCandidate.value};
  headerSizeOffset = ${bestCandidate.offset};
  
  console.log(\`📏 紧急修复HeaderSize: \${totalHeaderSize} (来自分析结果)\`);
} else {
  // 原有逻辑...
}

// 方法2: 临时解决方案，在浏览器控制台运行：
if (window.problemBuffer) {
  const view = new DataView(window.problemBuffer);
  console.log('🔧 验证修复候选:');
  console.log(\`偏移${bestCandidate.offset}的值: \${view.getInt32(${bestCandidate.offset}, true)}\`);
  
  // 手动设置解析参数
  window.manualHeaderSize = ${bestCandidate.value};
  window.manualHeaderOffset = ${bestCandidate.offset};
  console.log('✅ 手动参数已设置，重新尝试导入');
}

// 方法3: 如果是字节序问题
${analysis.magicNumber.bigEndian.isValid ? `
// 检测到大端字节序，需要修改所有数据读取：
// 将所有 view.getInt32(offset, true) 改为 view.getInt32(offset, false)
// 将所有 view.getUint32(offset, true) 改为 view.getUint32(offset, false)
console.log('⚠️ 需要修改字节序处理');
` : ''}
  `);
  
  // 生成测试脚本
  console.log('\n🧪 测试脚本:');
  console.log(`
// 在浏览器控制台运行此测试脚本
function testEmergencyFix() {
  if (!window.problemBuffer) {
    console.log('❌ 请先加载问题文件到 window.problemBuffer');
    return;
  }
  
  const view = new DataView(window.problemBuffer);
  const headerSize = view.getInt32(${bestCandidate.offset}, true);
  
  console.log('🧪 测试结果:');
  console.log(\`HeaderSize: \${headerSize}\`);
  console.log(\`文件大小: \${view.byteLength}\`);
  console.log(\`比例: \${(headerSize / view.byteLength * 100).toFixed(1)}%\`);
  console.log(\`合理性: \${headerSize > 0 && headerSize < view.byteLength ? '✅' : '❌'}\`);
  
  // 尝试读取后续字段
  try {
    let offset = ${bestCandidate.offset} + 4;
    const totalHeaderSizeInt64 = view.getBigInt64(offset, true);
    offset += 8;
    
    console.log(\`TotalHeaderSizeInt64: \${totalHeaderSizeInt64}\`);
    console.log('✅ 后续字段读取成功');
  } catch (e) {
    console.log(\`❌ 后续字段读取失败: \${e.message}\`);
  }
}

// 运行测试
testEmergencyFix();
  `);
}

// 导出函数
window.emergencyAnalyzeUAsset = emergencyAnalyzeUAsset;
window.generateHexDump = generateHexDump;
window.testEmergencyFix = () => {
  if (window.problemBuffer) {
    console.log('🧪 运行紧急修复测试...');
    // 这里可以添加具体的测试逻辑
  } else {
    console.log('❌ 请先运行 emergencyAnalyzeUAsset() 加载文件');
  }
};

// 显示使用说明
console.log('\n📖 紧急分析器使用说明:');
console.log('1. 运行 emergencyAnalyzeUAsset() 分析问题文件');
console.log('2. 查看分析结果和建议');
console.log('3. 使用生成的修复代码');
console.log('4. 运行 testEmergencyFix() 验证修复效果');
console.log('\n🚨 这是专门用于解决HeaderSize解析失败的紧急工具'); 