# UE .uasset文件格式深度分析

## 📋 概述

本文档基于Unreal Engine官方源码中的`PackageFileSummary.h`和`PackageFileSummary.cpp`，提供.uasset文件格式的权威解析方法。通过分析开源项目`uasset-rs`等基于UE源码的实现，总结出最准确的二进制文件解析方案。

## 🔍 问题分析

### 当前遇到的错误
```
错误信息: 无效的头部大小: 928956732, 文件大小: 53911
```

这个错误表明我们在错误的偏移位置读取了`TotalHeaderSize`字段，或者字节序处理有误。

### 根本原因
1. **UE版本差异**: 不同UE版本的.uasset格式略有差异
2. **字节对齐**: 某些字段可能需要按特定边界对齐
3. **条件字段**: 某些字段只在特定条件下存在
4. **Custom Versions**: UE5引入了更复杂的版本管理

## 🏗️ UE官方格式结构

### PackageFileSummary结构（基于UE源码）

```cpp
// 基于Engine/Source/Runtime/CoreUObject/Public/UObject/PackageFileSummary.h
struct FPackageFileSummary
{
    // 文件头标识
    uint32 Tag;                           // 0x9E2A83C1
    
    // 版本信息
    int32 LegacyFileVersion;              // 遗留文件版本
    int32 LegacyUE3Version;               // UE3版本（通常为-1）
    int32 FileVersionUE4;                 // UE4文件版本
    int32 FileVersionUE5;                 // UE5文件版本
    int32 LicenseeUE4Version;             // 授权版本
    
    // Custom Versions（UE4.16+）
    TArray<FCustomVersion> CustomVersionContainer;
    
    // 头部信息
    int32 TotalHeaderSize;                // 头部总大小
    FString FolderName;                   // 包文件夹名
    uint32 PackageFlags;                  // 包标志
    
    // 表信息
    int32 NameCount;                      // 名称表项数
    int32 NameOffset;                     // 名称表偏移
    int32 GatherableTextDataCount;        // 可收集文本数据数量（UE4.15+）
    int32 GatherableTextDataOffset;       // 可收集文本数据偏移（UE4.15+）
    int32 ExportCount;                    // 导出表项数
    int32 ExportOffset;                   // 导出表偏移
    int32 ImportCount;                    // 导入表项数
    int32 ImportOffset;                   // 导入表偏移
    int32 DependsOffset;                  // 依赖表偏移
    int32 SoftPackageReferencesCount;     // 软包引用数量（UE4.14+）
    int32 SoftPackageReferencesOffset;    // 软包引用偏移（UE4.14+）
    int32 SearchableNamesOffset;          // 可搜索名称偏移（UE4.??+）
    int32 ThumbnailTableOffset;           // 缩略图表偏移
    
    // GUID
    FGuid Guid;                           // 包GUID
    TArray<FGenerationInfo> Generations;  // 代际信息
    
    // Engine和Content版本
    int32 SavedByEngineVersion;           // 保存时引擎版本
    int32 CompatibleWithEngineVersion;    // 兼容引擎版本
    
    // 压缩信息
    uint32 CompressionFlags;              // 压缩标志
    TArray<FCompressedChunk> CompressedChunks; // 压缩块数组
    
    // Asset Registry相关
    int32 AssetRegistryDataOffset;        // 资产注册表数据偏移
    int64 BulkDataStartOffset;            // 大块数据起始偏移
    
    // 世界瓦片信息（UE4.15+）
    int32 WorldTileInfoDataOffset;        // 世界瓦片信息偏移
    
    // Chunk IDs（UE4.15+）
    TArray<int32> ChunkIDs;               // 块ID数组
    
    // Preload Dependencies（UE4.15+）
    int32 PreloadDependencyCount;         // 预加载依赖数量
    int32 PreloadDependencyOffset;        // 预加载依赖偏移
};
```

### 正确的解析顺序

```typescript
interface PackageFileSummary {
  // 基础头部
  tag: number                    // 4 bytes - 0x9E2A83C1
  legacyFileVersion: number      // 4 bytes
  legacyUE3Version: number       // 4 bytes  
  fileVersionUE4: number         // 4 bytes
  fileVersionUE5: number         // 4 bytes
  licenseeUE4Version: number     // 4 bytes
  
  // Custom Versions（条件存在）
  customVersions?: CustomVersion[]
  
  // 头部大小
  totalHeaderSize: number        // 4 bytes - 关键字段
  
  // 包信息
  folderName: string            // FString
  packageFlags: number          // 4 bytes
  
  // 表定义
  nameCount: number             // 4 bytes
  nameOffset: number            // 4 bytes
  
  // 根据版本条件存在的字段
  gatherableTextDataCount?: number
  gatherableTextDataOffset?: number
  
  exportCount: number           // 4 bytes
  exportOffset: number          // 4 bytes
  importCount: number           // 4 bytes
  importOffset: number          // 4 bytes
  dependsOffset: number         // 4 bytes
  
  // 更多条件字段...
}
```

## 🔧 修复方案

### 1. 基于uasset-rs的正确实现

参考`jorgenpt/uasset-rs`项目的实现，它直接基于UE源码：

```rust
// 来自 uasset-rs/src/package_file_summary.rs
impl PackageFileSummary {
    pub fn read<R: Read + Seek>(archive: &mut Archive<R>) -> Result<Self> {
        let tag = archive.read_u32::<LittleEndian>()?;
        if tag != PACKAGE_FILE_TAG {
            return Err(Error::InvalidPackageTag(tag));
        }
        
        let legacy_file_version = archive.read_i32::<LittleEndian>()?;
        let legacy_ue3_version = archive.read_i32::<LittleEndian>()?;
        let file_version_ue4 = archive.read_i32::<LittleEndian>()?;
        let file_version_ue5 = archive.read_i32::<LittleEndian>()?;
        let licensee_ue4_version = archive.read_i32::<LittleEndian>()?;
        
        // Custom versions handling
        let custom_versions = if file_version_ue4 >= VER_UE4_PACKAGE_SUMMARY_HAS_COMPATIBLE_ENGINE_VERSION {
            Some(archive.read_array(|a| CustomVersion::read(a))?)
        } else {
            None
        };
        
        let total_header_size = archive.read_i32::<LittleEndian>()?;
        
        // ... 继续读取其他字段
    }
}
```

### 2. 版本兼容性处理

不同UE版本的字段存在差异，需要根据版本号条件读取：

```typescript
const VER_UE4_OLDEST_LOADABLE_PACKAGE = 214;
const VER_UE4_PACKAGE_SUMMARY_HAS_COMPATIBLE_ENGINE_VERSION = 444;
const VER_UE4_ADD_STRING_ASSET_REFERENCES_MAP = 384;
const VER_UE4_SERIALIZE_TEXT_IN_PACKAGES = 459;

function readPackageFileSummary(view: DataView): PackageFileSummary {
    let offset = 0;
    
    // 基础字段
    const tag = view.getUint32(offset, true); offset += 4;
    const legacyFileVersion = view.getInt32(offset, true); offset += 4;
    const legacyUE3Version = view.getInt32(offset, true); offset += 4;
    const fileVersionUE4 = view.getInt32(offset, true); offset += 4;
    const fileVersionUE5 = view.getInt32(offset, true); offset += 4;
    const licenseeUE4Version = view.getInt32(offset, true); offset += 4;
    
    // 条件读取Custom Versions
    let customVersions: CustomVersion[] = [];
    if (fileVersionUE4 >= VER_UE4_PACKAGE_SUMMARY_HAS_COMPATIBLE_ENGINE_VERSION) {
        const customVersionCount = view.getInt32(offset, true); offset += 4;
        for (let i = 0; i < customVersionCount; i++) {
            // 读取CustomVersion结构
            // ...
        }
    }
    
    // 关键：TotalHeaderSize的位置取决于是否有CustomVersions
    const totalHeaderSize = view.getInt32(offset, true); offset += 4;
    
    // 验证TotalHeaderSize合理性
    if (totalHeaderSize < 0 || totalHeaderSize > view.byteLength) {
        throw new Error(`Invalid TotalHeaderSize: ${totalHeaderSize}, file size: ${view.byteLength}`);
    }
    
    // 继续读取FolderName（FString）
    const folderName = readFString(view, offset);
    offset = folderName.nextOffset;
    
    // ... 继续读取其他字段
}
```

### 3. FString正确读取

```typescript
function readFString(view: DataView, offset: number): { value: string; nextOffset: number } {
    if (offset + 4 > view.byteLength) {
        throw new Error('FString length field out of bounds');
    }
    
    const length = view.getInt32(offset, true);
    offset += 4;
    
    if (length === 0) {
        return { value: '', nextOffset: offset };
    }
    
    if (length < 0) {
        // Unicode字符串 (UTF-16)
        const actualLength = -length;
        const byteLength = actualLength * 2;
        
        if (offset + byteLength > view.byteLength) {
            throw new Error(`Unicode FString data out of bounds: need ${byteLength} bytes at offset ${offset}`);
        }
        
        // 读取UTF-16数据（排除null终止符）
        const utf16Array = new Uint16Array(view.buffer, view.byteOffset + offset, actualLength - 1);
        const value = String.fromCharCode(...Array.from(utf16Array));
        
        return { value, nextOffset: offset + byteLength };
    } else {
        // ASCII字符串 (UTF-8)
        const byteLength = length;
        
        if (offset + byteLength > view.byteLength) {
            throw new Error(`ASCII FString data out of bounds: need ${byteLength} bytes at offset ${offset}`);
        }
        
        // 读取UTF-8数据（排除null终止符）
        const utf8Array = new Uint8Array(view.buffer, view.byteOffset + offset, length - 1);
        const value = new TextDecoder('utf-8').decode(utf8Array);
        
        return { value, nextOffset: offset + byteLength };
    }
}
```

### 4. CustomVersion结构处理

```typescript
interface CustomVersion {
    key: string;    // FGuid as string
    version: number;
    friendlyName: string;
}

function readCustomVersion(view: DataView, offset: number): { version: CustomVersion; nextOffset: number } {
    // 读取GUID (16 bytes)
    const guidBytes = new Uint8Array(view.buffer, view.byteOffset + offset, 16);
    const key = Array.from(guidBytes).map(b => b.toString(16).padStart(2, '0')).join('');
    offset += 16;
    
    // 读取版本号
    const version = view.getInt32(offset, true);
    offset += 4;
    
    // 读取友好名称（可能不存在）
    const friendlyName = readFString(view, offset);
    offset = friendlyName.nextOffset;
    
    return {
        version: { key, version, friendlyName: friendlyName.value },
        nextOffset: offset
    };
}
```

## 🚀 完整解决方案

基于以上分析，我将创建一个修复脚本来正确解析.uasset文件：

```typescript
class UEPackageParser {
    static parse(buffer: ArrayBuffer): PackageFileSummary {
        const view = new DataView(buffer);
        let offset = 0;
        
        try {
            // 验证魔数
            const tag = view.getUint32(offset, true);
            if (tag !== 0x9E2A83C1) {
                throw new Error(`Invalid package tag: 0x${tag.toString(16)}`);
            }
            offset += 4;
            
            // 读取版本信息
            const legacyFileVersion = view.getInt32(offset, true); offset += 4;
            const legacyUE3Version = view.getInt32(offset, true); offset += 4;
            const fileVersionUE4 = view.getInt32(offset, true); offset += 4;
            const fileVersionUE5 = view.getInt32(offset, true); offset += 4;
            const licenseeUE4Version = view.getInt32(offset, true); offset += 4;
            
            console.log(`UE Versions: Legacy=${legacyFileVersion}, UE4=${fileVersionUE4}, UE5=${fileVersionUE5}`);
            
            // 处理CustomVersions（如果存在）
            let customVersions: CustomVersion[] = [];
            if (fileVersionUE4 >= 444) { // VER_UE4_PACKAGE_SUMMARY_HAS_COMPATIBLE_ENGINE_VERSION
                const customVersionCount = view.getInt32(offset, true);
                offset += 4;
                
                console.log(`Custom versions count: ${customVersionCount}`);
                
                for (let i = 0; i < customVersionCount; i++) {
                    const cvResult = readCustomVersion(view, offset);
                    customVersions.push(cvResult.version);
                    offset = cvResult.nextOffset;
                }
            }
            
            // 现在读取TotalHeaderSize
            const totalHeaderSize = view.getInt32(offset, true);
            offset += 4;
            
            console.log(`Total header size: ${totalHeaderSize} (at offset ${offset - 4})`);
            
            // 验证HeaderSize
            if (totalHeaderSize < 100 || totalHeaderSize > buffer.byteLength) {
                throw new Error(`Invalid header size: ${totalHeaderSize}, buffer size: ${buffer.byteLength}`);
            }
            
            // 继续解析...
            const folderName = readFString(view, offset);
            offset = folderName.nextOffset;
            
            return {
                tag,
                legacyFileVersion,
                legacyUE3Version,
                fileVersionUE4,
                fileVersionUE5,
                licenseeUE4Version,
                customVersions,
                totalHeaderSize,
                folderName: folderName.value,
                // ... 其他字段
            };
            
        } catch (error) {
            console.error(`Parse failed at offset ${offset}:`, error);
            throw error;
        }
    }
}
```

## 📚 参考资源

1. **官方源码**: Epic Games UnrealEngine GitHub repository
2. **开源解析器**: 
   - [uasset-rs](https://github.com/jorgenpt/uasset-rs) - Rust实现
   - [UE4Parse](https://github.com/FabianFG/UE4Parse) - C#实现
3. **格式文档**: 
   - Engine/Source/Runtime/CoreUObject/Public/UObject/PackageFileSummary.h
   - Engine/Source/Runtime/CoreUObject/Private/UObject/PackageFileSummary.cpp

通过直接参考UE源码和成熟的开源解析器，我们可以确保解析逻辑的准确性和兼容性。 