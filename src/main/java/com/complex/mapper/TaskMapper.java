package com.complex.mapper;

import com.complex.datacenter.Server;
import com.complex.entity.Job;
import com.complex.entity.OperationRecord;
import com.complex.utils.Page;

import java.util.HashMap;
import java.util.List;

public interface TaskMapper {


    void insertJobs(HashMap job);

    void insertCPU(HashMap keyset);

    List<String> queryAllCPUTime(HashMap map);

    List<Integer> queryAllCPU(HashMap map);

    List<HashMap> pageQueryData(Page page);

    int pageQueryCount(Page page);

    List<HashMap> queryAllServer(HashMap<String, Object> map);

    void insertCPURealTime(HashMap keyset);

    void insertJobs_real(Job job);

    List<HashMap> pageQueryJobData(Page page);

    int pageQueryJobCount(Page page);

    List<String> queryAllCPUTime_real(HashMap<String, Object> map);

    List<HashMap> queryAllCPU_real(HashMap<String, Object> map);

    List<HashMap> queryAllCPUTime_realSize();

    List<HashMap> queryAllCPUTimeSize();

    void insertPower(HashMap keyset);

    List<HashMap> pageQueryConsumptionData(Page page);

    int pageQueryConsumptionCount(Page page);

    List<HashMap> queryAllConsumption(HashMap<String, Object> map);

    List<HashMap> pageQueryJobData2(HashMap page);

    void deleteJobsByid(int id);

    void deleteCpusByid(Object id);

    List<HashMap> pageQueryLoggerData(Page page);

    int pageQueryLoggerCount(Page page);

    void deleteLoggerByid(int id);

    void insertSelective(OperationRecord operationRecord);
}
