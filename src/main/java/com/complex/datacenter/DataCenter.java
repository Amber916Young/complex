package com.complex.datacenter;

import com.complex.entity.Job;
import com.complex.utils.RandomID;
import com.complex.utils.TimeParse;

import java.util.*;

public class DataCenter {
    private Vector<Server> servers;
    private LinkedList<Job> jobs;

    public DataCenter() {
        this.servers = new Vector<Server>();
        this.jobs=new LinkedList<Job>();
    }

    public void addServer(final Server server) {
        this.servers.add(server);
    }

    public Vector<Server> getServers() {
        return this.servers;
    }

    public void initializtaion() {
        int nServers = 5;
        for(int i = 0; i < nServers; i++) {
            Server server_=new Server(2,2);
            this.addServer(server_);
        }
    }

    public void insertJobs(final double time,Job job){
        this.jobs.add(job);
    }

    public void allocateJobs(final double time) throws InterruptedException {
        for(Job job:jobs) {
            for (Server server : servers) {
                int remainingCapacity = server.getRemainingCapacity();
                if (remainingCapacity > 0 && server.getInstantUtilization() < 0.9) {
                    server.insertJob(time, job);
                }
            }
        }
        System.out.println(jobs.size()+"remains to be dealt with");
    }

    public void print(){
        Iterator<Server> iter=this.servers.iterator();
        while(iter.hasNext()){
            Server server=iter.next();
            server.print();
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DataCenter dataCenter = new DataCenter();
        Server server1 = null;

        double jobsnum=7;

        double divnum = jobsnum/4;
        double divDown =  Math.floor(divnum);
        int number_server = (int) divDown;
        int addition_num = (int) ((divnum-divDown)*4);

        Core preCore  = new Core();
        double idle_one = preCore.getPower(1)*6;
        double active_one = preCore.getPower(2)*2;
        double sum = active_one+idle_one;
        TimeParse timeParse = new TimeParse();
        String Time = timeParse.convertDate2String(new Date(),
                "yyyy-MM-dd HH:mm:ss");
        for (int i =0;i<number_server;i++){
            List<Job> jobList = new ArrayList<>();
            server1 = new Server(1,8);
            server1.setCid(33);

            server1.setCdate(Time);
        for (int k=0;k<4;k++){
                Job job=new Job(RandomID.genID());
                jobList.add(job);
            }
            server1.process2(System.currentTimeMillis()/1000,jobList);
            server1.getInstantUtilization();
        }
        Time = timeParse.convertDate2String(new Date(),
                "yyyy-MM-dd HH:mm:ss");
        if(addition_num!=0&&addition_num<=4){
            List<Job> jobList = new ArrayList<>();

            for (int k=0;k<addition_num;k++){
                Job job=new Job(RandomID.genID());
                jobList.add(job);
            }
            server1 = new Server(1,8);
            server1.setCid(333);
            server1.setCdate(Time);
            server1.process2(System.currentTimeMillis()/1000,jobList);
            server1.getInstantUtilization();
        }




    }
}
