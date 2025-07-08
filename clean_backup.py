#!/usr/bin/env python3
import sys

def clean_sql_file(input_file, output_file):
    try:
        # 尝试不同的编码方式读取文件
        encodings = ['utf-8', 'utf-16', 'latin-1', 'cp1252']
        
        content = None
        used_encoding = None
        
        for encoding in encodings:
            try:
                with open(input_file, 'r', encoding=encoding, errors='ignore') as f:
                    content = f.read()
                used_encoding = encoding
                print(f"成功使用 {encoding} 编码读取文件")
                break
            except:
                continue
        
        if content is None:
            # 如果所有编码都失败，使用二进制模式
            with open(input_file, 'rb') as f:
                raw_content = f.read()
            content = raw_content.decode('utf-8', errors='ignore')
            used_encoding = 'binary-utf8'
            print("使用二进制模式读取文件")
        
        # 清理问题字符
        content = content.replace('\x00', '')  # 移除空字符
        content = content.replace('\r\n', '\n')  # 标准化行结束符
        content = content.replace('\r', '\n')
        
        # 写入清理后的文件
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"文件清理完成: {output_file}")
        print(f"原始编码: {used_encoding}")
        return True
        
    except Exception as e:
        print(f"清理失败: {e}")
        return False

if __name__ == "__main__":
    input_file = "backup/database/tunnel_management_20250703_130056.sql"
    output_file = "backup/database/tunnel_management_cleaned.sql"
    
    if clean_sql_file(input_file, output_file):
        print("✅ 备份文件清理成功")
    else:
        print("❌ 备份文件清理失败")
