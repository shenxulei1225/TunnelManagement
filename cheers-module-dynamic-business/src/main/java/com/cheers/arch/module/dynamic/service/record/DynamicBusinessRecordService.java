package com.cheers.arch.module.dynamic.service.record;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.trees.service.TreeService;
import com.cheers.arch.module.dynamic.dal.dataobject.record.DynamicBusinessRecordDO;

import java.util.List;
import java.util.Map;

/**
 * 动态业务记录服务接口
 */
public interface DynamicBusinessRecordService extends TreeService<DynamicBusinessRecordDO> {

    Long createRecord(DynamicBusinessRecordDO record);

    void updateRecord(DynamicBusinessRecordDO record);

    void deleteRecord(Long id);

    DynamicBusinessRecordDO getRecord(Long id);

    List<DynamicBusinessRecordDO> getRecordList(List<Long> ids);

    PageResult<DynamicBusinessRecordDO> getRecordPage(Integer pageNo, Integer pageSize, String modelCode, Integer status);

    List<DynamicBusinessRecordDO> getRecordListByModelCode(String modelCode);

    List<DynamicBusinessRecordDO> getRecordTreeByModelCode(String modelCode);

    List<Map<String, Object>> getRecordTreeWithFields(String modelCode, List<String> fields);

    List<Long> batchCreateRecords(List<DynamicBusinessRecordDO> records);

    void batchUpdateRecords(List<DynamicBusinessRecordDO> records);

    void batchDeleteRecords(List<Long> ids);

    Map<String, Object> importRecords(String modelCode, List<Map<String, Object>> dataList);

    List<Map<String, Object>> exportRecords(String modelCode, List<Long> recordIds);

    boolean validateRecordData(DynamicBusinessRecordDO record);

    Map<String, Object> getRecordStatistics(String modelCode);

    boolean isValidParent(Long id, Long parentId);

    boolean isCodeUnique(String code, Long excludeId);
} 