# UMG文件导入指南

## 🎮 UE UMG 系统简介

UE UMG (Unreal Motion Graphics) 是Unreal Engine的用户界面系统，用于创建游戏UI、菜单和HUD等界面元素。

## 📁 支持的文件格式

### 1. .uasset 文件 (二进制格式)
- **文件类型**: Unreal Engine原生资产文件
- **包含内容**: Widget Blueprint的完整定义
- **文件大小**: 通常几KB到几MB
- **状态**: ⚠️ 部分支持（显示占位符和导出建议）

### 2. .json 文件 (推荐)
- **文件类型**: JSON格式的Widget数据
- **包含内容**: 组件结构、属性、样式等
- **文件大小**: 相对较小
- **状态**: ✅ 完全支持

## 🔄 从UE导出JSON格式

### 方法一：使用UE编辑器导出

1. **打开Widget Blueprint**
   - 在Content Browser中双击.uasset文件
   - 进入Widget Blueprint编辑器

2. **导出为JSON**
   ```
   File Menu → Export → Export Widget to JSON
   ```
   或使用快捷键: `Ctrl+Shift+E`

3. **保存JSON文件**
   - 选择保存位置
   - 文件名建议使用英文
   - 确认扩展名为`.json`

### 方法二：使用Python脚本导出

```python
import unreal

def export_widget_to_json(widget_path, output_path):
    """
    导出Widget Blueprint为JSON格式
    """
    # 加载Widget资产
    widget_asset = unreal.EditorAssetLibrary.load_asset(widget_path)
    
    if widget_asset:
        # 获取Widget数据
        widget_data = unreal.WidgetBlueprintLibrary.get_widget_data(widget_asset)
        
        # 导出为JSON
        json_string = unreal.JsonLibrary.stringify_object(widget_data)
        
        # 保存到文件
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(json_string)
        
        print(f"成功导出: {output_path}")
    else:
        print(f"无法加载Widget: {widget_path}")

# 使用示例
export_widget_to_json(
    "/Game/UI/MainMenu/MainMenuWidget",
    "C:/Temp/MainMenuWidget.json"
)
```

## 📊 JSON格式示例

### 标准UMG Widget结构
```json
{
  "className": "UCanvasPanel",
  "name": "MainCanvas",
  "slot": {
    "anchors": {
      "minimum": { "x": 0, "y": 0 },
      "maximum": { "x": 1, "y": 1 }
    },
    "offsets": {
      "left": 0,
      "top": 0, 
      "right": 0,
      "bottom": 0
    },
    "alignment": { "x": 0.5, "y": 0.5 },
    "autoSize": false
  },
  "properties": {
    "visibility": "Visible",
    "renderOpacity": 1.0
  },
  "children": [
    {
      "className": "UTextBlock",
      "name": "TitleText",
      "slot": {
        "anchors": {
          "minimum": { "x": 0.5, "y": 0.1 },
          "maximum": { "x": 0.5, "y": 0.1 }
        },
        "offsets": {
          "left": -100,
          "top": -15,
          "right": 100, 
          "bottom": 15
        }
      },
      "properties": {
        "text": "主菜单",
        "font": {
          "fontObject": "/Game/UI/Fonts/TitleFont.TitleFont",
          "size": 24,
          "typefaceFontName": "Bold"
        },
        "colorAndOpacity": {
          "r": 1, "g": 1, "b": 1, "a": 1
        }
      }
    },
    {
      "className": "UButton", 
      "name": "StartButton",
      "slot": {
        "anchors": {
          "minimum": { "x": 0.5, "y": 0.5 },
          "maximum": { "x": 0.5, "y": 0.5 }
        },
        "offsets": {
          "left": -75,
          "top": -20,
          "right": 75,
          "bottom": 20
        }
      },
      "properties": {
        "style": {
          "normal": {
            "tintColor": { "r": 0.2, "g": 0.6, "b": 1, "a": 1 }
          },
          "hovered": {
            "tintColor": { "r": 0.3, "g": 0.7, "b": 1, "a": 1 }
          }
        }
      },
      "children": [
        {
          "className": "UTextBlock",
          "name": "ButtonText",
          "properties": {
            "text": "开始游戏",
            "font": {
              "size": 16
            },
            "colorAndOpacity": {
              "r": 1, "g": 1, "b": 1, "a": 1
            }
          }
        }
      ]
    }
  ]
}
```

## 🔧 导入步骤

### 1. 准备文件
确保您的文件符合以下要求：
- **JSON文件**: 包含完整的Widget结构
- **uasset文件**: 确认文件未损坏
- **文件编码**: UTF-8编码（JSON文件）

### 2. 执行导入
1. **点击导入按钮**: 工具栏"导入设计"
2. **选择平台**: 选择"UE UMG" 
3. **选择文件**: 上传.json或.uasset文件
4. **配置选项**: 根据需要调整导入配置
5. **开始导入**: 点击"开始导入"

### 3. 配置选项说明
```
✅ 保持层级结构    - 维持UMG的父子关系
✅ 转换字体        - 将UE字体映射为Web字体
✅ 优化目标平台    - 根据目标平台调整组件
```

## 🎯 组件映射关系

| UMG类型 | 设计器组件 | 说明 |
|---------|-----------|------|
| `UTextBlock` | Text | 文本显示组件 |
| `UButton` | Button | 按钮组件 |
| `UImage` | Image | 图片显示组件 |
| `UEditableTextBox` | Input | 文本输入框 |
| `UCanvasPanel` | Container | 画布容器 |
| `UVerticalBox` | Container | 垂直布局容器 |
| `UHorizontalBox` | Container | 水平布局容器 |
| `UScrollBox` | Container | 滚动容器 |
| `UProgressBar` | ProgressBar | 进度条 |
| `USlider` | Slider | 滑块 |
| `UCheckBox` | Checkbox | 复选框 |

## ⚙️ 坐标系统转换

### UMG锚点系统 → 绝对坐标
```javascript
// 锚点转换公式
const absoluteX = parentWidth * anchors.minimum.x + offsets.left
const absoluteY = parentHeight * anchors.minimum.y + offsets.top
const width = (anchors.maximum.x - anchors.minimum.x) * parentWidth + 
              (offsets.right - offsets.left)
const height = (anchors.maximum.y - anchors.minimum.y) * parentHeight + 
               (offsets.bottom - offsets.top)
```

### 常见锚点预设
| 预设名称 | 锚点值 | 用途 |
|---------|--------|------|
| 左上角 | (0,0) → (0,0) | 固定在左上角 |
| 居中 | (0.5,0.5) → (0.5,0.5) | 屏幕中央 |
| 拉伸填充 | (0,0) → (1,1) | 填满父容器 |
| 底部居中 | (0.5,1) → (0.5,1) | 底部中央 |

## 🎨 样式转换

### 颜色格式转换
```json
// UMG颜色格式 (0-1范围)
"colorAndOpacity": {
  "r": 0.2, "g": 0.6, "b": 1.0, "a": 1.0
}

// 转换为Hex格式
"color": "#3399FF"
```

### 字体转换
```json
// UMG字体
"font": {
  "fontObject": "/Game/UI/Fonts/MyFont.MyFont",
  "size": 24,
  "typefaceFontName": "Bold"
}

// 转换为Web字体
"fontFamily": "MyFont, Arial, sans-serif"
"fontSize": 24
"fontWeight": "bold"
```

## 🚨 .uasset文件处理

当您上传.uasset文件时，系统会：

1. **检测文件类型**: 识别为二进制UE资产文件
2. **显示占位符**: 创建包含文件信息的占位符组件
3. **提供导出建议**: 指导如何从UE导出JSON格式

### 占位符组件内容
```
⚠️ UAsset二进制文件检测

检测到UE .uasset二进制文件 (123.45 KB)

建议操作步骤：
1. 在Unreal Engine中打开此Widget Blueprint
2. 选择File → Export → Export to JSON  
3. 重新导入JSON格式文件

或者联系开发团队获取.uasset文件解析支持。
```

## 🔍 故障排除

### 常见问题

#### 错误: "不是有效的JSON"
**原因**: 上传了二进制.uasset文件但系统尝试按JSON解析
**解决方案**: 
1. 确认选择了正确的平台（UE UMG）
2. 从UE导出JSON格式文件
3. 检查JSON文件语法是否正确

#### 导入后组件位置错误
**原因**: 锚点系统转换问题
**解决方案**:
1. 检查原始UMG的锚点设置
2. 在导入配置中启用"优化目标平台"
3. 手动调整组件位置

#### 字体显示异常
**原因**: UE字体在Web环境中不可用
**解决方案**:
1. 启用"转换字体"选项
2. 手动修改字体为Web安全字体
3. 上传自定义Web字体文件

#### 按钮样式丢失
**原因**: UMG按钮样式无法直接映射
**解决方案**:
1. 在属性面板重新设置按钮样式
2. 使用预设的按钮主题
3. 自定义CSS样式

## 💡 最佳实践

### 1. UMG设计准备
- **规范命名**: 使用描述性的组件名称
- **合理分组**: 使用Panel组织相关组件
- **标准尺寸**: 使用整数像素值避免模糊

### 2. 导出优化
- **简化结构**: 移除不必要的嵌套容器
- **资源路径**: 确保图片等资源路径正确
- **版本兼容**: 使用稳定的UE版本导出

### 3. 导入后处理
- **位置微调**: 检查并调整组件位置
- **样式优化**: 根据目标平台优化样式
- **交互重建**: 重新配置按钮点击等交互

## 🚀 高级功能

### 自定义组件映射
如果您有自定义UMG组件，可以扩展转换器：

```javascript
// 在umgConverter.ts中添加自定义映射
case 'UMyCustomWidget':
  componentType = 'custom-component'
  props = {
    customProperty: widget.properties.myProperty
  }
  break
```

### 批量转换工作流
1. **批量导出**: 使用UE Python脚本批量导出
2. **自动化转换**: 编写脚本自动导入转换
3. **版本管理**: 建立版本控制流程

---

*通过正确的文件格式和导入流程，您可以将UMG界面无缝迁移到Web平台。* 