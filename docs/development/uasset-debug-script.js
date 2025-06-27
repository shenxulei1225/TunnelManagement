/**
 * UAsset文件调试脚本
 * 
 * 用于测试和调试修复后的UAsset文件解析器
 * 使用方法：在浏览器控制台中运行此脚本
 */

// 十六进制查看器 - 显示文件的前几百字节
function hexDump(buffer, maxBytes = 512) {
  const view = new DataView(buffer);
  const bytes = Math.min(maxBytes, buffer.byteLength);
  let output = '';
  
  for (let i = 0; i < bytes; i += 16) {
    // 偏移地址
    const addr = i.toString(16).padStart(8, '0').toUpperCase();
    output += `${addr}: `;
    
    // 十六进制字节
    let hexPart = '';
    let asciiPart = '';
    
    for (let j = 0; j < 16; j++) {
      if (i + j < bytes) {
        const byte = view.getUint8(i + j);
        hexPart += byte.toString(16).padStart(2, '0').toUpperCase() + ' ';
        asciiPart += (byte >= 32 && byte <= 126) ? String.fromCharCode(byte) : '.';
      } else {
        hexPart += '   ';
        asciiPart += ' ';
      }
    }
    
    output += hexPart + ' | ' + asciiPart + '\n';
  }
  
  return output;
}

// 分析文件头部结构
function analyzeFileHeader(buffer) {
  const view = new DataView(buffer);
  const analysis = {
    fileSize: buffer.byteLength,
    isValidUAsset: false,
    magicNumber: null,
    versions: {},
    possibleOffsets: {
      totalHeaderSize: [],
      folderName: []
    },
    errors: [],
    warnings: []
  };
  
  try {
    // 检查魔数
    if (buffer.byteLength >= 4) {
      const tag = view.getUint32(0, true);
      analysis.magicNumber = '0x' + tag.toString(16).toUpperCase();
      analysis.isValidUAsset = (tag === 0x9E2A83C1);
      
      if (!analysis.isValidUAsset) {
        analysis.errors.push(`无效魔数: ${analysis.magicNumber}, 期望: 0x9E2A83C1`);
      }
    }
    
    // 读取版本信息
    if (buffer.byteLength >= 24) {
      analysis.versions = {
        legacyFileVersion: view.getInt32(4, true),
        legacyUE3Version: view.getInt32(8, true),
        fileVersionUE4: view.getInt32(12, true),
        fileVersionUE5: view.getInt32(16, true),
        licenseeUE4Version: view.getInt32(20, true)
      };
    }
    
    // 寻找可能的TotalHeaderSize位置
    // 通常在版本信息后面，可能跳过CustomVersions
    const possibleHeaderSizeOffsets = [24, 28, 32, 36, 40, 44, 48];
    
    for (const offset of possibleHeaderSizeOffsets) {
      if (offset + 4 <= buffer.byteLength) {
        const headerSize = view.getInt32(offset, true);
        if (headerSize > 100 && headerSize < buffer.byteLength) {
          analysis.possibleOffsets.totalHeaderSize.push({
            offset,
            value: headerSize,
            likelihood: (headerSize > 200 && headerSize < buffer.byteLength / 2) ? 'high' : 'medium'
          });
        }
      }
    }
    
    // 寻找可能的字符串开始位置（FolderName）
    for (let offset = 24; offset < Math.min(200, buffer.byteLength - 4); offset += 4) {
      const length = view.getInt32(offset, true);
      
      // 检查是否可能是FString长度
      if (length >= 0 && length <= 256) {
        if (offset + 4 + length <= buffer.byteLength) {
          try {
            // 尝试读取ASCII字符串
            const strBytes = new Uint8Array(buffer, offset + 4, Math.min(length - 1, 50));
            const isPrintable = Array.from(strBytes).every(b => b >= 32 && b <= 126);
            
            if (isPrintable && length > 1) {
              const str = new TextDecoder('utf-8').decode(strBytes);
              analysis.possibleOffsets.folderName.push({
                offset,
                length,
                value: str,
                type: 'ASCII'
              });
            }
          } catch (e) {
            // 忽略解码错误
          }
        }
      } else if (length < 0 && length >= -256) {
        // 检查是否是Unicode字符串
        const actualLength = -length;
        const byteLength = actualLength * 2;
        
        if (offset + 4 + byteLength <= buffer.byteLength && actualLength > 1) {
          try {
            const utf16Array = new Uint16Array(buffer, offset + 4, Math.min(actualLength - 1, 25));
            const isValidUnicode = Array.from(utf16Array).every(c => c > 0 && c < 0xFFFE);
            
            if (isValidUnicode) {
              const str = String.fromCharCode(...Array.from(utf16Array));
              analysis.possibleOffsets.folderName.push({
                offset,
                length,
                value: str,
                type: 'Unicode'
              });
            }
          } catch (e) {
            // 忽略解码错误
          }
        }
      }
    }
    
  } catch (error) {
    analysis.errors.push(`分析失败: ${error.message}`);
  }
  
  return analysis;
}

// 测试修复后的解析器
function testUAssetParser() {
  console.log('🔧 UAsset文件解析器测试工具');
  console.log('请使用以下步骤测试您的.uasset文件：\n');
  
  console.log('1. 首先上传文件进行基础分析：');
  console.log(`
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.uasset';
    input.onchange = (e) => {
      const file = e.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        const buffer = e.target.result;
        
        console.log('📁 文件信息：');
        console.log('文件名:', file.name);
        console.log('文件大小:', buffer.byteLength, 'bytes');
        
        console.log('\\n📊 十六进制转储（前512字节）：');
        console.log(hexDump(buffer));
        
        console.log('\\n🔍 文件结构分析：');
        const analysis = analyzeFileHeader(buffer);
        console.log(analysis);
        
        // 存储到全局变量用于进一步测试
        window.testBuffer = buffer;
        console.log('\\n✅ 文件已加载到 window.testBuffer, 可进行进一步测试');
      };
      reader.readAsArrayBuffer(file);
    };
    input.click();
  `);
  
  console.log('\n2. 文件加载后，测试解析器：');
  console.log(`
    // 基础解析测试
    if (window.testBuffer) {
      try {
        // 这里需要导入您的UMGConverter类
        // const converter = new UMGConverter();
        // const result = converter.convertFromUAsset(window.testBuffer);
        // console.log('解析结果:', result);
        
        console.log('请在页面设计器中导入此文件进行完整测试');
      } catch (error) {
        console.error('解析失败:', error);
        
        // 提供详细的错误分析
        console.log('\\n🚨 错误分析建议：');
        const analysis = analyzeFileHeader(window.testBuffer);
        
        if (!analysis.isValidUAsset) {
          console.log('- 文件魔数无效，可能不是.uasset文件');
        }
        
        if (analysis.possibleOffsets.totalHeaderSize.length === 0) {
          console.log('- 未找到合理的头部大小字段，文件可能损坏');
        } else {
          console.log('- 可能的头部大小位置：');
          analysis.possibleOffsets.totalHeaderSize.forEach(h => {
            console.log(\`  偏移 \${h.offset}: \${h.value} bytes (可能性: \${h.likelihood})\`);
          });
        }
        
        if (analysis.possibleOffsets.folderName.length > 0) {
          console.log('- 可能的包名位置：');
          analysis.possibleOffsets.folderName.forEach(f => {
            console.log(\`  偏移 \${f.offset}: "\${f.value}" (\${f.type})\`);
          });
        }
        
        console.log('\\n建议：请检查UE版本兼容性和文件完整性');
      }
    } else {
      console.log('请先上传.uasset文件');
    }
  `);
  
  console.log('\n3. 如果解析仍然失败，请运行详细调试：');
  console.log(`
    // 逐步调试解析过程
    if (window.testBuffer) {
      const view = new DataView(window.testBuffer);
      let offset = 0;
      
      console.log('🔍 逐步解析调试：');
      
      try {
        // 魔数
        const tag = view.getUint32(offset, true); offset += 4;
        console.log(\`魔数: 0x\${tag.toString(16)} (偏移: 0)\`);
        
        // 版本信息
        const versions = {
          legacy: view.getInt32(offset, true), offset: offset += 4,
          ue3: view.getInt32(offset, true), offset: offset += 4,
          ue4: view.getInt32(offset, true), offset: offset += 4,
          ue5: view.getInt32(offset, true), offset: offset += 4,
          licensee: view.getInt32(offset, true), offset: offset += 4
        };
        console.log('版本信息:', versions);
        console.log(\`当前偏移: \${offset}\`);
        
        // 检查是否有CustomVersions
        const hasCustomVersions = versions.ue4 >= 444;
        console.log(\`需要CustomVersions: \${hasCustomVersions}\`);
        
        if (hasCustomVersions) {
          const customVersionCount = view.getInt32(offset, true); offset += 4;
          console.log(\`CustomVersions数量: \${customVersionCount} (偏移: \${offset - 4})\`);
          
          // 跳过CustomVersions（简化调试）
          if (customVersionCount > 0 && customVersionCount < 100) {
            console.log('⚠️ 跳过CustomVersions详细解析，使用估算偏移');
            offset += customVersionCount * 24; // 估算每个CustomVersion约24字节
          }
        }
        
        // 尝试读取TotalHeaderSize
        console.log(\`尝试在偏移 \${offset} 读取TotalHeaderSize\`);
        const totalHeaderSize = view.getInt32(offset, true); offset += 4;
        console.log(\`TotalHeaderSize: \${totalHeaderSize}\`);
        
        if (totalHeaderSize > window.testBuffer.byteLength) {
          console.log('❌ HeaderSize过大，可能偏移计算错误');
          
          // 尝试其他可能的位置
          for (let testOffset = 24; testOffset <= 60; testOffset += 4) {
            if (testOffset + 4 <= window.testBuffer.byteLength) {
              const testSize = view.getInt32(testOffset, true);
              if (testSize > 100 && testSize < window.testBuffer.byteLength) {
                console.log(\`💡 可能的HeaderSize在偏移 \${testOffset}: \${testSize}\`);
              }
            }
          }
        } else {
          console.log('✅ HeaderSize看起来合理');
        }
        
      } catch (error) {
        console.error('调试过程出错:', error);
      }
    }
  `);
}

// 导出调试函数
window.hexDump = hexDump;
window.analyzeFileHeader = analyzeFileHeader;
window.testUAssetParser = testUAssetParser;

// 自动运行测试说明
testUAssetParser(); 