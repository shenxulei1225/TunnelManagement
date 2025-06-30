const fs = require('fs');
const path = require('path');

const filePath = 'tunnel-management-ui/src/views/ux-designer/workspace/index.vue';

// 读取文件内容
let content = fs.readFileSync(filePath, 'utf8');

// 删除重复的变量定义
content = content.replace(
  /\/\/ 画布面板配置\nconst canvasPanel = ref\(\{\n  width: 1920,\n  height: 1080,\n  x: 0,\n  y: 0\n\}\)\n\n/,
  ''
);

content = content.replace(
  /\/\/ 标尺显示状态\nconst showRulers = ref\(false\)\n\n/,
  ''
);

content = content.replace(
  /\/\/ 全屏模式状态\nconst isFullscreen = ref\(false\)\nconst showLeftPanelInFullscreen = ref\(false\)\nconst showRightPanelInFullscreen = ref\(false\)\n\n/,
  ''
);

// 删除重复的loadFiles函数定义（如果存在）
content = content.replace(
  /\/\/ 获取文件列表[\s\S]*?(?=\/\/ 组件初始化时加载文件)/,
  '// 文件管理功能已迁移到 useFileManager 组合式函数\n\n'
);

// 写回文件
fs.writeFileSync(filePath, content, 'utf8');

console.log('重复变量定义已删除！'); 