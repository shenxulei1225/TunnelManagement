# UMG导出与转换指南

## 📖 概述

Unreal Engine的UMG系统不直接支持JSON导出，但我们可以通过多种方式获取UMG数据用于Universal X Designer导入。

## 🎯 支持的导出方式

### 1. T3D文本格式导出（推荐）

#### 操作步骤：
1. 在UE编辑器中打开UMG Widget蓝图
2. 选择要导出的Widget组件
3. 右键菜单选择"Copy"或使用Ctrl+C
4. 创建新的文本文件，粘贴内容
5. 保存为`.t3d`文件

#### T3D格式特点：
- ✅ 人类可读的文本格式
- ✅ 包含完整的组件层级信息
- ✅ 保留属性和样式设置
- ✅ 支持复杂的嵌套结构

### 2. 蓝图序列化数据

#### 通过UE的C++/蓝图API获取：
```cpp
// C++示例代码
FString GetWidgetAsJson(UUserWidget* Widget)
{
    FJsonObject JsonObject;
    
    // 遍历Widget树
    if (Widget->WidgetTree && Widget->WidgetTree->RootWidget)
    {
        TSharedPtr<FJsonObject> RootJson = SerializeWidget(Widget->WidgetTree->RootWidget);
        JsonObject.SetObjectField("RootWidget", RootJson);
    }
    
    // 转换为JSON字符串
    FString OutputString;
    TSharedRef<TJsonWriter<>> Writer = TJsonWriterFactory<>::Create(&OutputString);
    FJsonSerializer::Serialize(JsonObject.ToSharedRef(), Writer);
    
    return OutputString;
}
```

### 3. 自定义导出插件

#### 推荐的UE插件方案：
```cpp
// 插件代码示例
UCLASS()
class UMGEXPORTER_API UUMGExporter : public UObject
{
    GENERATED_BODY()

public:
    UFUNCTION(BlueprintCallable, Category = "UMG Exporter")
    static FString ExportWidgetToJson(UUserWidget* Widget);
    
    UFUNCTION(BlueprintCallable, Category = "UMG Exporter")
    static bool SaveWidgetAsJson(UUserWidget* Widget, const FString& FilePath);
};
```

## 🔧 实际操作指南

### 方法一：T3D导出（最简单）

1. **选择根Widget**
   ```
   在UMG设计器中选择根Canvas Panel或主容器
   ```

2. **复制组件树**
   ```
   右键 → Copy 或 Ctrl+C
   ```

3. **保存为T3D文件**
   ```
   创建文本文件 → 粘贴 → 另存为 .t3d
   ```

### 方法二：使用UE命令行

```bash
# 使用UE命令行工具导出
UnrealEditor.exe ProjectName -run=ExportWidgets -WidgetPath=/Game/UI/MainMenu -OutputFormat=T3D
```

### 方法三：编辑器脚本导出

```python
# UE Python脚本示例
import unreal

def export_widget_to_json(widget_path, output_path):
    # 加载Widget资产
    widget_asset = unreal.EditorAssetLibrary.load_asset(widget_path)
    
    if widget_asset:
        # 获取Widget树数据
        widget_tree = widget_asset.widget_tree
        
        # 序列化为JSON（需要自定义序列化逻辑）
        json_data = serialize_widget_tree(widget_tree)
        
        # 保存到文件
        with open(output_path, 'w') as f:
            f.write(json_data)
            
        print(f"导出成功: {output_path}")
    else:
        print(f"无法加载Widget: {widget_path}")

# 使用示例
export_widget_to_json("/Game/UI/MainMenu", "D:/MainMenu.json")
```

## 📋 T3D格式解析示例

### 典型的T3D文件结构：
```t3d
Begin Object Class=/Script/UMG.CanvasPanel Name="CanvasPanel_0"
   Anchors=(Minimum=(X=0.000000,Y=0.000000),Maximum=(X=1.000000,Y=1.000000))
   Offsets=(Left=0.000000,Top=0.000000,Right=0.000000,Bottom=0.000000)
   
   Begin Object Class=/Script/UMG.Button Name="Button_0"
      WidgetStyle=(Normal=(ResourceObject=/Engine/EngineMaterials/Widget_Button_Normal))
      Content=Begin Object Class=/Script/UMG.TextBlock Name="TextBlock_0"
         Text=NSLOCTEXT("", "ButtonText", "Click Me")
         Font=(FontObject=/Engine/EngineFonts/Roboto,Size=24)
      End Object
   End Object
   
End Object
```

## 🔄 在UXD中导入

### 支持的导入格式：
- ✅ `.t3d` - T3D文本格式
- ✅ `.uasset` - UE资产文件
- ✅ `.json` - 自定义JSON格式
- ✅ `.txt` - 复制的组件数据

### 导入步骤：
1. 打开Universal X Designer
2. 点击"导入"按钮
3. 选择"UMG"标签
4. 上传T3D或其他支持的文件
5. 配置转换选项
6. 开始导入转换

## ⚙️ 高级配置

### T3D解析器配置：
```typescript
interface T3DParserOptions {
  preserveHierarchy: boolean;     // 保持层级结构
  convertMaterials: boolean;      // 转换材质
  scaleFactors: {                 // 缩放因子
    position: number;
    size: number;
  };
  fontMapping: Record<string, string>; // 字体映射
}
```

### 自定义组件映射：
```typescript
const umgComponentMapping = {
  'CanvasPanel': 'CanvasContainer',
  'Button': 'Button',
  'TextBlock': 'Text',
  'Image': 'Image',
  'VerticalBox': 'VerticalContainer',
  'HorizontalBox': 'HorizontalContainer'
};
```

## 🎯 最佳实践

### 1. 导出前准备
- 确保Widget结构清晰
- 命名规范化
- 移除不必要的复杂材质

### 2. 导出优化
- 使用相对定位而非绝对定位
- 简化动画和特效
- 统一字体和颜色规范

### 3. 导入后处理
- 检查组件映射
- 调整样式细节
- 测试响应式布局

## 🔧 故障排除

### 常见问题：

**Q: T3D文件过大怎么办？**
A: 可以分割导出，或移除不必要的材质资源

**Q: 复杂动画无法转换？**
A: 建议先导出静态布局，后续手动添加动画

**Q: 字体无法识别？**
A: 使用字体映射功能，将UE字体映射到Web字体

**Q: 材质丢失？**
A: T3D格式不包含材质资源，需要单独导出图片

## 📚 参考资源

- [UE官方UMG文档](https://docs.unrealengine.com/5.3/en-US/umg-ui-designer-user-guide/)
- [T3D格式规范](https://docs.unrealengine.com/4.27/en-US/Resources/ContentExamples/ExportingToText/)
- [UE Python API文档](https://docs.unrealengine.com/5.3/en-US/python-api/) 