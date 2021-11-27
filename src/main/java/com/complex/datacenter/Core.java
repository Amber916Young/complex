package com.complex.datacenter;
import com.complex.entity.Job;
import lombok.SneakyThrows;

import java.sql.Timestamp;
import java.util.Date;

public class Core {
    public Job job;
   
    private double speed;
    private Socket socket;
    private int coreId;

    private double dynamicPower;
    private double parkPower;
    private double idlePower;

    public Core() {
        this.job = null;
        this.speed = 2.0;
        dynamicPower = 20.0 * (4.0 / 8.0) / 2;
        parkPower = 0;
        idlePower = dynamicPower / 8.0;
    }
    public double getPower(int type) {
//        if (this.job != null){
//            return this.dynamicPower - this.idlePower;
//        }else{
//            return this.idlePower;
//        }
        if (type==2){
            return this.dynamicPower - this.idlePower;
        }else{
            return this.idlePower;
        }
    }
    public void print(){
        if(this.job!=null) {
            this.job.print();
        } else{
            System.out.println("No job in this core!");
        }
    }

    public void assignJob(final double time, Job onejob){
        if (this.job != null) {
            System.out.println("Already has a job!");
        }else{
            this.job=onejob;
            double startTime=time;
            Timestamp t = new Timestamp(new Date().getTime());
            job.markStart(startTime);
            job.setStartDate(t.toString());
        }
    }

    @SneakyThrows
    public void process(double time) {
        if (this.job != null){
            int currentProgress=0;
            if(this.speed==0){
                job=null;
                System.out.println("speed is 0! cannot handle any job!");
                return;
            }
            while(currentProgress<job.getJobSize()){
                currentProgress+=this.speed;
                Thread.sleep(100);
//                System.err.println("111===》"+getPower(2));
            }
            if(currentProgress>=job.getJobSize()){
                this.job.markFinish(time+currentProgress/this.speed);
                Timestamp t = new Timestamp(new Date().getTime());
                job.setFinishDate(t.toString());
                this.job.print();
                System.out.println("Job "+this.job.getJobId()+" is finished.");
                this.job=null;
//                System.err.println("222===》"+getPower(1));
            }
        }
    }

    public void removeJob(final double time, final Job oneJob){
        if (this.job != null) {
            System.out.println("Error!No job in this core!");
        }else{
            double finishTime=time+this.job.getJobSize()/this.speed;
            job.markFinish(finishTime);
            this.job=null;
        }
    }

    public Job getJob(){
        return this.job;
    }

//    public AJAXReturn STARTWORK(Core core,int num){
//        Job job=new Job(num);
//        long time= new Date().getTime();
//        AJAXReturn ajaxReturn = new AJAXReturn();
//        core.assignJob(time,job);
//        core.process(time);
//        ajaxReturn.setErrcode(0);
//        ajaxReturn.setErrmsg("execution successful");
//        ajaxReturn.setData(JsonUtils.objectToJson(job));
//        if(job.getFinishDate().equals(null)||job.getFinishDate().equals("")){
//            ajaxReturn.setErrmsg("something cash!");
//            ajaxReturn.setErrcode(2);
//        }
//        return ajaxReturn;
//    }

    public static void main(String[] args) throws InterruptedException {
        Core core= new Core();
//        core.STARTWORK(core,5);
        Job job=new Job(5);
//        TimeParse timeParse = new TimeParse();
        Timestamp t = new Timestamp(new Date().getTime());
        long time= new Date().getTime();
        core.assignJob(time,job);
        core.process(time);
    }

}
