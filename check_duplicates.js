const fs = require('fs');
const path = require('path');

// 读取工作台文件
const filePath = path.join(__dirname, 'tunnel-management-ui', 'src', 'views', 'ux-designer', 'workspace', 'index.vue');

try {
  const content = fs.readFileSync(filePath, 'utf8');
  const lines = content.split('\n');
  
  // 搜索loadFiles相关的声明
  const loadFilesReferences = [];
  
  lines.forEach((line, index) => {
    if (line.includes('loadFiles')) {
      loadFilesReferences.push({
        lineNumber: index + 1,
        content: line.trim()
      });
    }
  });
  
  console.log('找到的loadFiles引用:');
  loadFilesReferences.forEach(ref => {
    console.log(`第${ref.lineNumber}行: ${ref.content}`);
  });
  
  // 检查重复的常量声明
  const constDeclarations = {};
  lines.forEach((line, index) => {
    const constMatch = line.match(/const\s+(\w+)\s*=/);
    if (constMatch) {
      const varName = constMatch[1];
      if (!constDeclarations[varName]) {
        constDeclarations[varName] = [];
      }
      constDeclarations[varName].push({
        lineNumber: index + 1,
        content: line.trim()
      });
    }
  });
  
  // 输出重复的声明
  console.log('\n重复的const声明:');
  Object.keys(constDeclarations).forEach(varName => {
    if (constDeclarations[varName].length > 1) {
      console.log(`\n变量 "${varName}" 重复声明:`);
      constDeclarations[varName].forEach(decl => {
        console.log(`  第${decl.lineNumber}行: ${decl.content}`);
      });
    }
  });
  
} catch (error) {
  console.error('读取文件失败:', error.message);
} 