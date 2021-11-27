//package com.complex.datacenter;
//
//import com.complex.entity.Job;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.Vector;
//import java.util.Iterator;
//
//public class Socket {
//
//    private Vector<Core> availableCores;
//    private Vector<Core> busyCores;
//    private int cores;
//    private ConcurrentHashMap<Job,Core>jobToCore;
//
//    public void print(){
//        for(Core core:availableCores){
//            core.print();
//        }
//    }
//
//    public Socket(final int theNCores) {
//        this.cores = theNCores;
//
//        this.jobToCore = new ConcurrentHashMap<Job, Core>();
//        this.availableCores = new Vector<Core>();
//        this.busyCores = new Vector<Core>();
//
//        for (int i = 0; i < cores; i++) {
//            Core core = new Core();
//            this.availableCores.add(core);
//        }
//
//    }
//
//    public Vector<Core> getAvailableCores(){
//        return this.availableCores;
//    }
//
//    public int getRemainingCapacity() {
//        return this.availableCores.size();
//    }
//
//    public int getTotalCapacity() {
//        return this.cores;
//    }
//
//    public int getJobsInService() {
//        return this.busyCores.size();
//    }
//
//    public double getInstantUtilization() {
//        return ((double) this.busyCores.size() ) / this.cores;
//    }
//
//    public void addJob(final double time,final Job oneJob){
//        if(availableCores.size()>0)
//        {   Iterator<Core> iter = availableCores.iterator();
//            Core core = iter.next();
//            this.busyCores.add(core);
//            this.jobToCore.put(oneJob, core);
//            availableCores.remove(core);
//        }else{
//            System.out.println("No avaliable cores!");
//        }
//    }
//
//    public void removeJob(final double time,
//                          final Job oneJob){
//        Core core = this.jobToCore.remove(oneJob);
//        if (core == null) {
//            System.out.println("Error!Cannot find the core!");
//        }else{
//            boolean found = this.busyCores.remove(core);
//            if (!found) {
//                System.out.println("Error!Cannot find the busy core!");
//            }
//            this.availableCores.add(core);
//        }
//    }
//
//    public void process(double time) throws InterruptedException {
//        if(jobToCore.size()==0){
//            System.out.println("No job, free time!");
//        }else{
//            for(ConcurrentHashMap.Entry<Job,Core>entry : jobToCore.entrySet()){
//                Job job=entry.getKey();
//                Core core=entry.getValue();
//                core.assignJob(time,job);
//                core.process(time);
//
//                if(core.getJob()==null){
//                    this.removeJob(System.currentTimeMillis()/1000,job);
//                }else{
//                    core.getJob().finishPrint();
//                }
//            }
//
//        }
//    }
//
//    public double getPower() {
//        double power=0.0;
//        Iterator<Core> coreIter = this.getAvailableCores().iterator();
//        while (coreIter.hasNext()) {
//            Core core = coreIter.next();
//            double corePower = core.getPower();
//            power += corePower;
//        }
//        return power;
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        Server server=new Server(2,2);
//        Socket socket=new Socket(3);
//        Job job=new Job(5);
//        Job job2=new Job(3);
//        long time=System.currentTimeMillis()/1000;
//        socket.addJob(time,job);
//        socket.addJob(time,job2);
//
//        socket.process(time);
//    }
//}