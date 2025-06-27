/**
 * 自适应解析器专用测试脚本
 * 用于验证新的智能字段搜索和错误恢复功能
 */

(function() {
  'use strict';

  console.log('🧪 自适应解析器测试工具已加载');

  // 全局测试函数
  window.testAdaptiveParser = function(fileInput) {
    if (!fileInput || !fileInput.files || fileInput.files.length === 0) {
      console.error('❌ 请选择一个.uasset文件');
      return;
    }

    const file = fileInput.files[0];
    console.log(`📁 测试文件: ${file.name} (${file.size} bytes)`);

    const reader = new FileReader();
    reader.onload = function(e) {
      const buffer = e.target.result;
      
      console.log('\n=== 🧠 自适应解析器测试 ===');
      testAndCompareAllParsers(buffer);
    };

    reader.onerror = function() {
      console.error('❌ 文件读取失败');
    };

    reader.readAsArrayBuffer(file);
  };

  function testAndCompareAllParsers(buffer) {
    console.log(`📊 文件大小: ${buffer.byteLength} bytes`);
    
    // 1. 测试自适应解析器
    console.log('\n--- 🧠 自适应解析器 ---');
    const adaptiveResult = testParser('Adaptive', () => UMGConverter.testAdaptiveParser(buffer));

    // 2. 测试版本感知解析器
    console.log('\n--- 🎯 版本感知解析器 ---');
    const versionAwareResult = testParser('VersionAware', () => UMGConverter.testVersionAwareParser(buffer));

    // 3. 生成对比报告
    console.log('\n=== 📋 对比报告 ===');
    generateComparisonReport(adaptiveResult, versionAwareResult);

    // 4. 如果自适应解析器成功，显示详细结果
    if (adaptiveResult && adaptiveResult.success) {
      console.log('\n=== ✅ 自适应解析器成功结果 ===');
      displayDetailedResults(adaptiveResult);
    }

    // 5. 进行完整分析
    console.log('\n--- 🔬 完整文件分析 ---');
    try {
      const analysis = UMGConverter.analyzeUAssetFile(buffer);
      console.log('文件分析结果:', analysis);
    } catch (e) {
      console.error('文件分析失败:', e);
    }
  }

  function testParser(name, testFn) {
    try {
      console.time(`${name}解析时间`);
      const result = testFn();
      console.timeEnd(`${name}解析时间`);
      
      if (result && result.success) {
        console.log(`✅ ${name}解析器: 成功`);
        console.log(`   Import数量: ${result.importCount || 'N/A'}`);
        console.log(`   Export数量: ${result.exportCount || 'N/A'}`);
        console.log(`   Name数量: ${result.nameCount || 'N/A'}`);
        
        if (result.strategy) {
          console.log(`   使用策略: ${result.strategy}`);
        }
        
        return result;
      } else {
        console.log(`❌ ${name}解析器: 失败`);
        if (result && result.error) {
          console.log(`   错误: ${result.error}`);
        }
        return null;
      }
    } catch (error) {
      console.error(`💥 ${name}解析器异常:`, error);
      return null;
    }
  }

  function generateComparisonReport(adaptiveResult, versionAwareResult) {
    const adaptiveSuccess = adaptiveResult && adaptiveResult.success;
    const versionAwareSuccess = versionAwareResult && versionAwareResult.success;

    console.log('┌─────────────────────┬─────────────┬───────────────┐');
    console.log('│ 解析器类型          │ 状态        │ Import数量    │');
    console.log('├─────────────────────┼─────────────┼───────────────┤');
    console.log(`│ 自适应解析器        │ ${adaptiveSuccess ? '✅ 成功' : '❌ 失败'}    │ ${adaptiveResult?.importCount || 'N/A'}           │`);
    console.log(`│ 版本感知解析器      │ ${versionAwareSuccess ? '✅ 成功' : '❌ 失败'}    │ ${versionAwareResult?.importCount || 'N/A'}           │`);
    console.log('└─────────────────────┴─────────────┴───────────────┘');

    // 推荐建议
    if (adaptiveSuccess && !versionAwareSuccess) {
      console.log('💡 建议: 自适应解析器解决了版本感知解析器无法处理的问题');
    } else if (!adaptiveSuccess && versionAwareSuccess) {
      console.log('⚠️  注意: 版本感知解析器成功，但自适应解析器失败，请检查自适应逻辑');
    } else if (adaptiveSuccess && versionAwareSuccess) {
      console.log('🎉 两个解析器都成功！请验证结果一致性');
      
      // 检查结果一致性
      if (adaptiveResult.importCount !== versionAwareResult.importCount) {
        console.warn(`⚠️  Import数量不一致: 自适应=${adaptiveResult.importCount}, 版本感知=${versionAwareResult.importCount}`);
      }
    } else {
      console.log('❌ 两个解析器都失败，需要进一步分析文件格式');
    }
  }

  function displayDetailedResults(result) {
    if (result.result) {
      const summary = result.result;
      console.log('📋 详细解析结果:');
      console.log(`   TotalHeaderSize: ${summary.totalHeaderSize}`);
      console.log(`   FolderName: "${summary.folderName}"`);
      console.log(`   PackageFlags: 0x${summary.packageFlags?.toString(16)}`);
      console.log(`   NameCount: ${summary.nameCount}, NameOffset: ${summary.nameOffset}`);
      console.log(`   ExportCount: ${summary.exportCount}, ExportOffset: ${summary.exportOffset}`);
      console.log(`   ImportCount: ${summary.importCount}, ImportOffset: ${summary.importOffset}`);
      console.log(`   版本: UE4=${summary.fileVersionUE4}, UE5=${summary.fileVersionUE5}`);
      
      // 检查数值合理性
      checkReasonableness(summary);
    }
  }

  function checkReasonableness(summary) {
    console.log('\n🔍 数值合理性检查:');
    
    const checks = [
      {
        name: 'TotalHeaderSize',
        value: summary.totalHeaderSize,
        condition: v => v > 0 && v < 10000,
        message: '应该在1-10000范围内'
      },
      {
        name: 'ImportCount',
        value: summary.importCount,
        condition: v => v >= 0 && v < 50000,
        message: '应该在0-50000范围内'
      },
      {
        name: 'ExportCount', 
        value: summary.exportCount,
        condition: v => v >= 0 && v < 50000,
        message: '应该在0-50000范围内'
      },
      {
        name: 'NameCount',
        value: summary.nameCount,
        condition: v => v > 0 && v < 50000,
        message: '应该在1-50000范围内'
      }
    ];

    checks.forEach(check => {
      const isReasonable = check.condition(check.value);
      const status = isReasonable ? '✅' : '⚠️ ';
      console.log(`   ${status} ${check.name}: ${check.value} ${isReasonable ? '' : '- ' + check.message}`);
    });
  }

  // 快捷测试命令
  window.quickTestAdaptive = function() {
    const fileInput = document.querySelector('input[type="file"]');
    if (fileInput) {
      testAdaptiveParser(fileInput);
    } else {
      console.log('💡 使用方法:');
      console.log('1. 在页面上选择一个.uasset文件');
      console.log('2. 运行: testAdaptiveParser(document.querySelector("input[type=file]"))');
      console.log('或者:');
      console.log('3. 使用: window.testFileBuffer = yourArrayBuffer; UMGConverter.testAdaptiveParser(window.testFileBuffer)');
    }
  };

  // 直接测试缓冲区
  window.testBufferAdaptive = function(buffer) {
    if (!buffer) {
      console.error('❌ 请提供ArrayBuffer');
      return;
    }
    
    console.log('🧪 直接测试ArrayBuffer...');
    testAndCompareAllParsers(buffer);
  };

  console.log('📚 可用命令:');
  console.log('  - testAdaptiveParser(fileInput) - 测试文件输入');
  console.log('  - quickTestAdaptive() - 快速测试页面文件');
  console.log('  - testBufferAdaptive(buffer) - 直接测试缓冲区');
  console.log('  - UMGConverter.testAdaptiveParser(buffer) - 直接调用解析器');

})(); 