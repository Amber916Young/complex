package com.complex.service;


import com.complex.datacenter.Server;
import com.complex.entity.Job;
import com.complex.entity.OperationRecord;
import com.complex.mapper.TaskMapper;
import com.complex.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskMapper taskMapper;


    public void insertJobs(HashMap job) {
        taskMapper.insertJobs(job);
    }

    public void insertCPU(HashMap keyset) {
        taskMapper.insertCPU(keyset);
    }

    public List<String> queryAllCPUTime(HashMap map) {
        return taskMapper.queryAllCPUTime(map);
    }

    public List<Integer> queryAllCPU(HashMap map) {
        return taskMapper.queryAllCPU(map);
    }

    public List<HashMap> pageQueryData(Page page) {
        return taskMapper.pageQueryData(page);
    }

    public int pageQueryCount(Page page) {
        return taskMapper.pageQueryCount(page);
    }

    public List<HashMap> queryAllServer(HashMap<String, Object> map) {
        return taskMapper.queryAllServer(map);
    }

    public void insertCPURealTime(HashMap keyset) {
        taskMapper.insertCPURealTime(keyset);
    }

    public void insertJobs_real(Job job) {
        taskMapper.insertJobs_real(job);

    }

    public List<HashMap> pageQueryJobData(Page page) {
        return taskMapper.pageQueryJobData(page);
    }

    public int pageQueryJobCount(Page page) {
        return taskMapper.pageQueryJobCount(page);

    }

    public List<String> queryAllCPUTime_real(HashMap<String, Object> map) {
        return taskMapper.queryAllCPUTime_real(map);
    }

    public List<HashMap> queryAllCPU_real(HashMap<String, Object> map) {
        return taskMapper.queryAllCPU_real(map);
    }

    public List<HashMap> queryAllCPUTime_realSize() {
        return taskMapper.queryAllCPUTime_realSize();
    }

    public List<HashMap> queryAllCPUTimeSize() {
        return taskMapper.queryAllCPUTimeSize();
    }

    public void insertPower(HashMap keyset) {
        taskMapper.insertPower(keyset);
    }

    public List<HashMap> pageQueryConsumptionData(Page page) {
        return taskMapper.pageQueryConsumptionData(page);
    }

    public int pageQueryConsumptionCount(Page page) {
        return taskMapper.pageQueryConsumptionCount(page);
    }

    public List<HashMap> queryAllConsumption(HashMap<String, Object> map) {
        return taskMapper.queryAllConsumption(map);

    }

    public List<HashMap> pageQueryJobData2(HashMap page) {
        return taskMapper.pageQueryJobData2(page);
    }

    public void deleteJobsByid(int id) {
        taskMapper.deleteJobsByid(id);

    }

    public void deleteCpusByid(Object id) {
        taskMapper.deleteCpusByid(id);
    }

    public List<HashMap> pageQueryLoggerData(Page page) {
        return taskMapper.pageQueryLoggerData(page);
    }

    public int pageQueryLoggerCount(Page page) {
        return taskMapper.pageQueryLoggerCount(page);
    }

    public void deleteLoggerByid(int id) {
         taskMapper.deleteLoggerByid(id);
    }

    public void insertSelective(OperationRecord operationRecord) {
        taskMapper.insertSelective(operationRecord);
    }
}
