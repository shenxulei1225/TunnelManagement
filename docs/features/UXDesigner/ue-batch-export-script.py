#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
UE Widget Blueprint 批量导出脚本
用于批量导出UE项目中的Widget Blueprint为JSON格式

使用方法:
1. 将此脚本放在UE项目根目录
2. 安装依赖: pip install requests pathlib
3. 运行: python ue-batch-export-script.py

支持的导出方式:
- 直接复制.uasset文件
- 通过UE Python API导出JSON
- 通过UnrealPak工具提取
"""

import os
import json
import shutil
import subprocess
from pathlib import Path
from typing import List, Dict, Any
import argparse

class UEWidgetExporter:
    def __init__(self, project_path: str, output_dir: str = "exported_widgets"):
        self.project_path = Path(project_path)
        self.content_path = self.project_path / "Content"
        self.output_dir = Path(output_dir)
        self.output_dir.mkdir(exist_ok=True)
        
    def find_widget_assets(self) -> List[Path]:
        """查找所有Widget Blueprint资产文件"""
        widget_files = []
        
        # 搜索.uasset文件
        for uasset_file in self.content_path.rglob("*.uasset"):
            # 检查是否为Widget Blueprint
            if self.is_widget_blueprint(uasset_file):
                widget_files.append(uasset_file)
                
        return widget_files
    
    def is_widget_blueprint(self, asset_file: Path) -> bool:
        """检查资产文件是否为Widget Blueprint"""
        try:
            # 读取文件头部分
            with open(asset_file, 'rb') as f:
                # 检查UE资产文件标识符
                magic = int.from_bytes(f.read(4), byteorder='little')
                if magic != 0x9E2A83C1:
                    return False
                
                # 简单检查是否包含Widget相关的名称
                # 实际项目中可以实现更精确的检测
                f.seek(0)
                content = f.read(1024).decode('utf-8', errors='ignore')
                widget_keywords = ['UserWidget', 'WidgetBlueprint', 'UMG']
                return any(keyword in content for keyword in widget_keywords)
                
        except Exception:
            return False
    
    def copy_uasset_files(self, widget_files: List[Path]):
        """复制.uasset文件到输出目录"""
        uasset_dir = self.output_dir / "uasset_files"
        uasset_dir.mkdir(exist_ok=True)
        
        for widget_file in widget_files:
            # 计算相对路径
            rel_path = widget_file.relative_to(self.content_path)
            output_path = uasset_dir / rel_path
            output_path.parent.mkdir(parents=True, exist_ok=True)
            
            # 复制.uasset文件
            shutil.copy2(widget_file, output_path)
            print(f"已复制: {rel_path}")
            
            # 查找并复制对应的.uexp文件
            uexp_file = widget_file.with_suffix('.uexp')
            if uexp_file.exists():
                uexp_output = output_path.with_suffix('.uexp')
                shutil.copy2(uexp_file, uexp_output)
                print(f"已复制: {rel_path.with_suffix('.uexp')}")
    
    def generate_ue_python_script(self) -> str:
        """生成UE Python导出脚本"""
        script_content = """
import unreal
import json
import os

def export_widget_to_json(asset_path, output_path):
    '''导出Widget Blueprint为JSON格式'''
    
    # 加载资产
    asset = unreal.EditorAssetLibrary.load_asset(asset_path)
    if not asset:
        print(f"无法加载资产: {asset_path}")
        return False
    
    # 检查是否为Widget Blueprint
    if not isinstance(asset, unreal.WidgetBlueprint):
        print(f"不是Widget Blueprint: {asset_path}")
        return False
    
    # 获取Widget Tree
    widget_tree = asset.widget_tree
    if not widget_tree:
        print(f"Widget Tree为空: {asset_path}")
        return False
    
    # 导出为JSON
    widget_data = {
        'assetPath': asset_path,
        'className': asset.get_class().get_name(),
        'name': asset.get_name(),
        'widgetTree': export_widget_tree(widget_tree)
    }
    
    # 保存JSON文件
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(widget_data, f, indent=2, ensure_ascii=False)
    
    print(f"已导出: {asset_path} -> {output_path}")
    return True

def export_widget_tree(widget_tree):
    '''递归导出Widget Tree结构'''
    if not widget_tree:
        return None
    
    # 获取根Widget
    root_widget = widget_tree.root_widget
    if not root_widget:
        return None
    
    return export_widget_node(root_widget)

def export_widget_node(widget):
    '''导出单个Widget节点'''
    if not widget:
        return None
    
    node_data = {
        'className': widget.get_class().get_name(),
        'name': getattr(widget, 'name', 'Unknown'),
        'properties': {},
        'slot': {},
        'children': []
    }
    
    # 导出Widget属性
    try:
        # 位置和尺寸信息
        if hasattr(widget, 'slot'):
            slot = widget.slot
            if slot:
                node_data['slot'] = {
                    'anchors': {
                        'minimum': {'x': slot.anchors.minimum.x, 'y': slot.anchors.minimum.y},
                        'maximum': {'x': slot.anchors.maximum.x, 'y': slot.anchors.maximum.y}
                    },
                    'offsets': {
                        'left': slot.offsets.left,
                        'top': slot.offsets.top,
                        'right': slot.offsets.right,
                        'bottom': slot.offsets.bottom
                    },
                    'alignment': {'x': slot.alignment.x, 'y': slot.alignment.y}
                }
        
        # 文本内容（如果是文本控件）
        if hasattr(widget, 'text'):
            node_data['properties']['text'] = str(widget.text)
        
        # 颜色信息
        if hasattr(widget, 'color_and_opacity'):
            color = widget.color_and_opacity
            node_data['properties']['color'] = {
                'r': color.r, 'g': color.g, 'b': color.b, 'a': color.a
            }
            
    except Exception as e:
        print(f"导出属性时出错: {e}")
    
    # 导出子Widget
    try:
        if hasattr(widget, 'get_all_children'):
            children = widget.get_all_children()
            for child in children:
                child_data = export_widget_node(child)
                if child_data:
                    node_data['children'].append(child_data)
    except Exception as e:
        print(f"导出子节点时出错: {e}")
    
    return node_data

def main():
    '''主导出函数'''
    
    # 获取所有Widget Blueprint资产
    asset_registry = unreal.AssetRegistryHelpers.get_asset_registry()
    filter = unreal.ARFilter(
        class_names=["WidgetBlueprint"],
        recursive_paths=True
    )
    
    assets = asset_registry.get_assets(filter)
    
    output_dir = "D:/exported_widgets/json_files"  # 修改为你的输出目录
    os.makedirs(output_dir, exist_ok=True)
    
    exported_count = 0
    for asset in assets:
        asset_path = str(asset.object_path)
        asset_name = asset.asset_name
        
        output_file = os.path.join(output_dir, f"{asset_name}.json")
        
        if export_widget_to_json(asset_path, output_file):
            exported_count += 1
    
    print(f"导出完成! 共导出 {exported_count} 个Widget Blueprint")

# 运行导出
if __name__ == "__main__":
    main()
"""
        return script_content
    
    def create_ue_python_export(self):
        """创建UE Python导出脚本文件"""
        script_path = self.output_dir / "ue_export_widgets.py"
        
        with open(script_path, 'w', encoding='utf-8') as f:
            f.write(self.generate_ue_python_script())
        
        print(f"已创建UE Python导出脚本: {script_path}")
        print("使用方法:")
        print("1. 在UE编辑器中打开Python控制台")
        print("2. 执行: exec(open(r'{}').read())".format(script_path.absolute()))
        
        return script_path
    
    def extract_with_unrealpak(self, pak_file: Path, output_dir: Path):
        """使用UnrealPak工具提取.pak文件中的Widget资产"""
        
        if not pak_file.exists():
            print(f"PAK文件不存在: {pak_file}")
            return
        
        # 查找UnrealPak工具
        unrealpak_path = self.find_unrealpak_tool()
        if not unrealpak_path:
            print("未找到UnrealPak工具")
            return
        
        print(f"使用UnrealPak提取: {pak_file}")
        
        try:
            # 执行提取命令
            cmd = [
                str(unrealpak_path),
                str(pak_file),
                "-Extract",
                str(output_dir),
                "-Filter=*.uasset"
            ]
            
            result = subprocess.run(cmd, capture_output=True, text=True)
            
            if result.returncode == 0:
                print("提取成功!")
                print(result.stdout)
            else:
                print("提取失败:")
                print(result.stderr)
                
        except Exception as e:
            print(f"执行UnrealPak时出错: {e}")
    
    def find_unrealpak_tool(self) -> Path:
        """查找UnrealPak工具路径"""
        
        # 常见的UE安装路径
        common_paths = [
            "C:/Program Files/Epic Games/UE_5.3/Engine/Binaries/Win64/UnrealPak.exe",
            "C:/Program Files/Epic Games/UE_5.2/Engine/Binaries/Win64/UnrealPak.exe",
            "C:/Program Files/Epic Games/UE_5.1/Engine/Binaries/Win64/UnrealPak.exe",
            "C:/Program Files/Epic Games/UE_5.0/Engine/Binaries/Win64/UnrealPak.exe",
        ]
        
        for path in common_paths:
            if Path(path).exists():
                return Path(path)
        
        # 在项目目录中查找
        for unrealpak in self.project_path.rglob("UnrealPak.exe"):
            return unrealpak
            
        return None
    
    def export_all(self, export_uasset: bool = True, export_json: bool = True):
        """执行完整的导出流程"""
        
        print("开始搜索Widget Blueprint资产...")
        widget_files = self.find_widget_assets()
        
        if not widget_files:
            print("未找到Widget Blueprint资产文件")
            return
        
        print(f"找到 {len(widget_files)} 个Widget Blueprint文件")
        
        if export_uasset:
            print("\n复制.uasset文件...")
            self.copy_uasset_files(widget_files)
        
        if export_json:
            print("\n创建UE Python导出脚本...")
            self.create_ue_python_export()
        
        # 生成导出报告
        self.generate_export_report(widget_files)
        
        print(f"\n导出完成! 输出目录: {self.output_dir.absolute()}")
    
    def generate_export_report(self, widget_files: List[Path]):
        """生成导出报告"""
        
        report = {
            'exportDate': '2024-01-20',
            'projectPath': str(self.project_path),
            'totalWidgets': len(widget_files),
            'widgets': []
        }
        
        for widget_file in widget_files:
            rel_path = widget_file.relative_to(self.content_path)
            
            widget_info = {
                'name': widget_file.stem,
                'path': str(rel_path),
                'size': widget_file.stat().st_size,
                'hasUexp': widget_file.with_suffix('.uexp').exists()
            }
            
            report['widgets'].append(widget_info)
        
        # 保存报告
        report_path = self.output_dir / "export_report.json"
        with open(report_path, 'w', encoding='utf-8') as f:
            json.dump(report, f, indent=2, ensure_ascii=False)
        
        print(f"导出报告已保存: {report_path}")

def main():
    parser = argparse.ArgumentParser(description="UE Widget Blueprint 批量导出工具")
    parser.add_argument("project_path", help="UE项目根目录路径")
    parser.add_argument("-o", "--output", default="exported_widgets", help="输出目录")
    parser.add_argument("--no-uasset", action="store_true", help="不复制.uasset文件")
    parser.add_argument("--no-json", action="store_true", help="不生成JSON导出脚本")
    
    args = parser.parse_args()
    
    exporter = UEWidgetExporter(args.project_path, args.output)
    exporter.export_all(
        export_uasset=not args.no_uasset,
        export_json=not args.no_json
    )

if __name__ == "__main__":
    main() 