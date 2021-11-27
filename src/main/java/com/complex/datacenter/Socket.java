package com.complex.datacenter;

import com.complex.entity.Job;
import com.complex.service.TaskService;
import com.complex.utils.SpringUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Vector;
import java.util.Iterator;

@Data
public class Socket {


    private Vector<Core> availableCores;
    private Vector<Core> busyCores;
    private int cores;
    public ConcurrentHashMap<Job,Core>jobToCore;

    public void print(){
        for(Core core:availableCores){
            core.print();
        }
    }

    public Socket(final int theNCores) {
        this.cores = theNCores;
        this.jobToCore = new ConcurrentHashMap<Job, Core>();
        this.availableCores = new Vector<Core>();
        this.busyCores = new Vector<Core>();
        for (int i = 0; i < cores; i++) {
            Core core = new Core();
            this.availableCores.add(core);
        }

    }

    public Vector<Core> getBusyCores(){
        return this.busyCores;
    }
    public Vector<Core> getAvailableCores(){
        return this.availableCores;
    }

    public int getRemainingCapacity() {
        return this.availableCores.size();
    }

    public int getTotalCapacity() {
        return this.cores;
    }

    public int getJobsInService() {
        return this.busyCores.size();
    }

    public double getInstantUtilization() {
        return ((double) this.busyCores.size() ) / this.cores;
    }

    public void addJob(final double time,final Job oneJob){
        Iterator<Core> iter = availableCores.iterator();
        Core core = iter.next();
        this.busyCores.add(core);
        this.jobToCore.put(oneJob, core);
        availableCores.remove(core);
    }


    public synchronized void  removeJob(final double time,
                          final Job oneJob){
        Core core = null;
        ConcurrentHashMap<Job,Core> tempjobToCore = new ConcurrentHashMap<>();
        for (Iterator<Map.Entry<Job, Core>> iterator =
             this.jobToCore.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Job,Core> entry = iterator.next();
            Job key =entry.getKey();
            Core value = entry.getValue();
            this.jobToCore.remove(key);
            if (key.equals(oneJob)){
                core = value;
                iterator.remove();
                System.out.println(this.jobToCore);
            }else {
                tempjobToCore.put(key, value);
            }
        }
        System.out.println(tempjobToCore);
        this.jobToCore = tempjobToCore;
        if (core == null) {
            System.out.println("Error!Cannot find the core!");
        }else{
            boolean found = this.busyCores.remove(core);
            if (!found) {
                System.out.println("Error!Cannot find the busy core!");
            }
            this.availableCores.add(core);
        }
    }
    public HashMap<String, Double> getPower() {
        double power=0.0;
        double power2=0.0;
        HashMap map = new HashMap();
        Iterator<Core> idleCoreIter = this.getAvailableCores().iterator();
        while (idleCoreIter.hasNext()) {
            Core core = idleCoreIter.next();
            double corePower = core.getPower(1);
            power += corePower;
        }
        map.put("idle",power);
        Iterator<Core> busyCoreIter = this.getBusyCores().iterator();
        while (busyCoreIter.hasNext()) {
            Core core = busyCoreIter.next();
            double corePower = core.getPower(2);
            power2 += corePower;
        }
        map.put("busy",power2);
        return map;
    }

    TaskService taskService = (TaskService) SpringUtil.getBean("taskService");

    public synchronized void process(double time) {
        if(jobToCore.size()==0){
            System.out.println("No job, free time!");
        }else{
            ConcurrentHashMap<Job,Core> tempjobToCore =new ConcurrentHashMap<>();
            for(ConcurrentHashMap.Entry<Job,Core> entry : jobToCore.entrySet()){
                tempjobToCore.put(entry.getKey(),entry.getValue());
            }
            for(ConcurrentHashMap.Entry<Job,Core> entry : tempjobToCore.entrySet()){
                Job job=entry.getKey();
                Core core=entry.getValue();
                core.assignJob(time,job);
                core.process(time);
                if(core.getJob()==null){
                    System.out.println(job);
                    taskService.insertJobs_real(job);
                    this.removeJob(System.currentTimeMillis()/1000,job);
                }else{
                    core.getJob().print();
                }
            }

        }
    }

    public static void main(String[] args) {
//        Server server=new Server(2,2);
        Socket socket=new Socket(6);
        Job job=new Job(4);
        Job job2=new Job(2);
//        Job job3=new Job(6);
        long time=System.currentTimeMillis()/1000;
        socket.addJob(time,job);
        socket.addJob(time,job2);
//        socket.addJob(time,job3);

        socket.process(time);
    }
}