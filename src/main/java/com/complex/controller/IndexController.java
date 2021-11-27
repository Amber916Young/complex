package com.complex.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.complex.datacenter.Core;
import com.complex.datacenter.DataCenter;
import com.complex.datacenter.Server;
import com.complex.entity.Job;
import com.complex.service.TaskService;
import com.complex.utils.*;
import com.github.abel533.echarts.DataZoom;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Toolbox;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.CrossStyle;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private TaskService taskService;
    TimeParse timeParse = new TimeParse();
    private String Delcode="root";
    @GetMapping("index/login")
    public Object LoginIndex(){
        return "UserLogin";
    }
    @GetMapping("index/logger")
    public Object loggerndex(){
        return "logger_index";
    }
    @GetMapping("index/Consumption")
    public Object ConsumptionIndex(){
        return "comsuptiom_index";
    }
    @GetMapping("index/Consumption/chart")
    public Object ConsumptionchartIndex(){
        return "chart_consumption_index";
    }
    @GetMapping("index/userList")
    public Object userListIndex(){
        return "user_index";
    }

    @GetMapping("index/jobs")
    public Object JobsIndex(){
        return "jobs_index";
    }


    @GetMapping("index/cpu")
    public Object CPUIndex(){
        return "cpu_index";
    }

    @SneakyThrows
    private void LoadTest (int number_server, List<HashMap> serverList,int addition_num){
        Server server1 = null;
        int len = serverList.size();
        String Time = timeParse.convertDate2String(new Date(),
                "yyyy-MM-dd HH:mm:ss");

        for(int i=0;i<number_server;i++){
            List<Job> jobList = new ArrayList<>();
            HashMap serverTemp =  serverList.get(i);
            int tot_cores =Integer.parseInt(serverTemp.get("numberOfSockets").toString())*Integer.parseInt(serverTemp.get("numberOfCoresPerSocket").toString());
            server1 = new Server(1,tot_cores);
            server1.setCid(Integer.parseInt(serverTemp.get("serverId").toString()));
            server1.setCdate(Time);
            for(int k=0;k<tot_cores/2;k++){
                Job job=new Job(RandomID.genID());
                jobList.add(job);
            }
            server1.process2(System.currentTimeMillis()/1000,jobList);
            server1.getInstantUtilization();
        }
        for(int i=number_server+1;i<=serverList.size();i++){
            HashMap keyset = new HashMap();
            keyset.put("cid",i);
            double utilization =getUtilization();
            keyset.put("utilization",utilization);
            keyset.put("cdate",Time);
            keyset.put("idle",getIdleDe());
            keyset.put("busy",0.0);
            double total_power = getIdleDe();
            keyset.put("total_power",total_power);
            taskService.insertCPURealTime(keyset);
            taskService.insertPower(keyset);
        }

        Time = timeParse.convertDate2String(new Date(),
                "yyyy-MM-dd HH:mm:ss");
        if(addition_num!=0&&addition_num<=len/2){
            List<Job> jobList = new ArrayList<>();
            for (int k=0;k<addition_num;k++){
                Job job=new Job(RandomID.genID());
                jobList.add(job);
            }
            HashMap serverTemp = serverList.get(0);
            int tot_cores =Integer.parseInt(serverTemp.get("numberOfSockets").toString())*Integer.parseInt(serverTemp.get("numberOfCoresPerSocket").toString());
            server1 = new Server(1,tot_cores);
            server1.setCid(Integer.parseInt(serverTemp.get("serverId").toString()));
            server1.setCdate(Time);
            server1.process2(System.currentTimeMillis()/1000,jobList);
            server1.getInstantUtilization();
            for(int i=2;i<=serverList.size();i++){
                HashMap keyset = new HashMap();
                keyset.put("cid",i);
                double utilization =getUtilization();
                keyset.put("utilization",utilization);
                keyset.put("cdate",Time);
                keyset.put("idle",getIdleDe());
                keyset.put("busy",0.0);
                double total_power = getIdleDe();
                keyset.put("total_power",total_power);
                taskService.insertCPURealTime(keyset);
                taskService.insertPower(keyset);
            }
        }

    }

    @SneakyThrows
    @ResponseBody
    @PostMapping("energy/load")
    public Object realEnergy(@RequestBody String jsonData) {
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=", "");
        HashMap<String, Object> map = JSONObject.parseObject(jsonData, HashMap.class);
        int pageNO = Integer.parseInt(map.get("pageno").toString());
        int longNum = 7;
        pageNO = (pageNO-1)*7;
        map.put("pageno",pageNO);
        map.put("rows", longNum );
        HashMap retuen = new HashMap<>();
        List<HashMap> list = taskService.queryAllConsumption(map);
        if(list.size()==0){
            map.put("pageno",0);
            list = taskService.queryAllConsumption(map);
            retuen.put("pageno", 1);

        }
        List<Double> temp1 = new ArrayList<>();
        List<Double> temp2 = new ArrayList<>();
        List<Double> temp3 = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            double idle = (double)list.get(i).get("idle");
            double busy = (double)list.get(i).get("busy");
            double total_power = (double)list.get(i).get("total_power");
            temp1.add(idle);
            temp2.add(busy);
            temp3.add(total_power);
        }
        retuen.put("seriesT1", temp1);
        retuen.put("seriesT2", temp2);
        retuen.put("seriesT3", temp3);
        return AJAXReturn.success(retuen);
    }

    @SneakyThrows
    @ResponseBody
    @PostMapping("jobs/assign")
    public Object JOBAssign(@RequestBody String jsonData){
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=", "");
        HashMap<String, Object> map = new HashMap<>();
        if(!jsonData.equals("")||!jsonData.equals(null)) {
            map = JSONObject.parseObject(jsonData, HashMap.class);
        }
        double jobsnum=12.0;
        if(map.containsKey("job_num")){
            jobsnum = Integer.parseInt(map.get("job_num").toString());
        }
        List<HashMap> serverList = taskService.queryAllServer(map);
        double divnum = jobsnum/4;
        double divDown =  Math.floor(divnum);
        int number_server = (int) divDown;
        int addition_num = (int) ((divnum-divDown)*4);
        Core preCore  = new Core();
        double idle_one = preCore.getPower(1)*2;
        double active_one = preCore.getPower(2)*6;
        double sum = active_one+idle_one;
        int loop = 0;
        if(number_server<=serverList.size()){
            loop=number_server;
            LoadTest(loop,serverList,addition_num);

        }else{
            number_server = number_server%serverList.size();
        }
        return AJAXReturn.success("all jobs finished!! Please view the list");
    }
    private double getIdleDe (){
        double dynamicPower = 20.0 * (4.0 / 8.0) / 2;
        double idlePower = dynamicPower / 8.0;
        double idle =idlePower*8;
        return idle;
    }

    private double getUtilization (){
        double utilization =0.0;
        switch (RandomID.getCPUID()){
            case 0:
                utilization  = RandomID.getCPUNum();
                break;
            case 1:
                utilization  = RandomID.getCPUNum2();
                break;
            case 2:
                utilization  = RandomID.getCPUNum3();
                break;
            case 3:
                utilization  = RandomID.getCPUNum4();
                break;
        }
        return utilization;
    }

    @SneakyThrows
    @ResponseBody
    @GetMapping("job/log/lastOne/num/{num}")
    public Object JOBSLOGLoadLastOne(@PathVariable("num") int  num, Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        HashMap map = new HashMap();
        int totals=taskService.pageQueryJobCount(page);
        int start = totals-num-1;
        map.put("start",start);
        map.put("rows",num);
        List<HashMap> lists = taskService.pageQueryJobData2(map);
        return  AJAXReturn.success("successfully",lists,num);
    }

    @SneakyThrows
    @ResponseBody
    @GetMapping("job/log")
    public Object JOBSLOGLoad( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        String keyword = request.getParameter("keyword");
        page.setRows(limit);
        page.setKeyWord(keyword);
        List<HashMap> lists = taskService.pageQueryJobData(page);
        int totals=taskService.pageQueryJobCount(page);
        return  AJAXReturn.success("successfully",lists,totals);
    }


    @ResponseBody
    @PostMapping(value = "/job/deletes")
    public Object jobDeletes(@RequestBody String jsonData)  throws UnsupportedEncodingException {
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=","");
        JSONObject json = JSONObject.parseObject(jsonData);
        String res = "";
        JSONArray ids = json.getJSONArray("ids");
        String code = json.getString("code");
        if (code.equals(Delcode)) {
            for (int i = 0; i < ids.size(); i++) {
                int id = (int) ids.get(i);
                taskService.deleteJobsByid(id);
            }
            res = "successfully!";
            return AJAXReturn.success(res);
        } else {
            res = "Code is wrong，delete fail!";
            return AJAXReturn.error(res);
        }
    }


    @SneakyThrows
    @ResponseBody
    @GetMapping("logger/log")
    public Object loggerLoad( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        String keyword = request.getParameter("keyword");
        page.setRows(limit);
        page.setKeyWord(keyword);
        List<HashMap> lists = taskService.pageQueryLoggerData(page);
        int totals=taskService.pageQueryLoggerCount(page);
        return  AJAXReturn.success("successfully",lists,totals);
    }

    @ResponseBody
    @PostMapping(value = "/logger/deletes")
    public Object loggerDeletes(@RequestBody String jsonData)  throws UnsupportedEncodingException {
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=","");
        JSONObject json = JSONObject.parseObject(jsonData);
        String res = "";
        JSONArray ids = json.getJSONArray("ids");
        String code = json.getString("code");
        if (code.equals(Delcode)) {
            for (int i = 0; i < ids.size(); i++) {
                int id = (int) ids.get(i);
                taskService.deleteLoggerByid(id);
            }
            res = "successfully!";
            return AJAXReturn.success(res);
        } else {
            res = "Code is wrong，delete fail!";
            return AJAXReturn.error(res);
        }
    }


    @ResponseBody
    @PostMapping(value = "cpu/log/deletes")
    public Object CPUlogDeletes(@RequestBody String jsonData)  throws UnsupportedEncodingException {
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=","");
        JSONObject json = JSONObject.parseObject(jsonData);
        String res = "";
        JSONArray ids = json.getJSONArray("ids");
        String code = json.getString("code");
        if (code.equals(Delcode)) {
            for (int i = 0; i < ids.size(); i++) {
                Object id =  ids.get(i);
                taskService.deleteCpusByid(id);
            }
            res = "successfully!";
            return AJAXReturn.success(res);
        } else {
            res = "Code is wrong，delete fail!";
            return AJAXReturn.error(res);
        }
    }
    @SneakyThrows
    @ResponseBody
    @GetMapping("cpu/log")
    public Object CPULOGLoad( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        String keyword = request.getParameter("keyword");
        page.setRows(limit);
        page.setKeyWord(keyword);
        List<HashMap> lists = taskService.pageQueryData(page);
        int totals=taskService.pageQueryCount(page);
        return  AJAXReturn.success("successfully",lists,totals);
    }
    @SneakyThrows
    @ResponseBody
    @GetMapping("consumption/log")
    public Object comsumptionLOGLoad( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        String keyword = request.getParameter("keyword");
        page.setRows(limit);
        page.setKeyWord(keyword);
        List<HashMap> lists = taskService.pageQueryConsumptionData(page);
        int totals=taskService.pageQueryConsumptionCount(page);
        return  AJAXReturn.success("successfully",lists,totals);
    }


    /*
     * desc  generate fake cpu utilization
     * */
    @SneakyThrows
    @ResponseBody
    @GetMapping ("simulation/cpu")
    public Object Simulation(){
        HashMap keyset = new HashMap();
        List<HashMap> serverList = taskService.queryAllServer(keyset);
        int len = serverList.size();
            String Time = timeParse.convertDate2String(new Date(),
                    "yyyy-MM-dd HH:mm:ss");
            for(int k=1;k<=len;k++){
                Thread.sleep(800);
                double utilization =getUtilization();

//                if(i<=20){
//
//                }else if(i>20&&i<=40) {
//                    utilization  = RandomID.getCPUNum2();
//                }else if(i>40&&i<=60) {
//                    utilization  = RandomID.getCPUNum3();
//                }else if(i>60&&i<=80) {
//                    utilization  = RandomID.getCPUNum4();
//                }else if(i>80&&i<=100) {
//                    utilization  = RandomID.getCPUNum5();
//                }
                keyset.put("cid",k);
                keyset.put("utilization",utilization);
                keyset.put("cdate",Time);
                taskService.insertCPURealTime(keyset);
            }
        return AJAXReturn.success();
    }

    @SneakyThrows
    @ResponseBody
    @PostMapping("cpu/load/real")
    public Object realCPU(@RequestBody String jsonData) {
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=", "");
        HashMap<String, Object> map = JSONObject.parseObject(jsonData, HashMap.class);
        int pageNO = Integer.parseInt(map.get("pageno").toString());
        int longNum = 30;
        map.put("rows", longNum );
        List legendName = new ArrayList();
        HashMap retuen = new HashMap<>();
        List<String> listTimew = taskService.queryAllCPUTime_real(map);
        if (listTimew.size() < longNum) {
            map.put("pageno",0);
            retuen.put("pageno", 1);
        }
        List<String> listTime = new ArrayList<>();
        boolean flag =false;
        List temp2 = new ArrayList<>();
        List<HashMap> serverList = taskService.queryAllServer(map);
        int len = serverList.size();
        for (int i = 0; i < len; i++) {
            HashMap serverTemp = serverList.get(i);
            map.put("cid", Integer.parseInt(serverTemp.get("serverId").toString()));
            HashMap series = new HashMap<>();
            List<HashMap> list = taskService.queryAllCPU_real(map);
            List<Double> utilizationCPU = new ArrayList<>();
            for(int n=0;n<list.size();n++){
                if (!flag){
                    listTime.add(list.get(n).get("cdate").toString());

                }
                double utilization = Double.parseDouble(list.get(n).get("utilization").toString());
                utilizationCPU.add(utilization);
            }
            flag=true;
            int nnnn = i+1;
            legendName.add("CPU" + nnnn);
            series.put("name", "CPU" + nnnn);
            series.put("type", "line");
            series.put("connectNulls", true);
            series.put("data",utilizationCPU);
            temp2.add(series);
        }

        retuen.put("legendT", legendName);
        retuen.put("xAxisT", listTime);
        retuen.put("seriesT", temp2);
        return AJAXReturn.success(retuen);
    }

    @SneakyThrows
    @ResponseBody
    @PostMapping("cpu/load")
    public Object loadCPUIndex(@RequestBody String jsonData){
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=", "");
        HashMap<String, Object> map = JSONObject.parseObject(jsonData, HashMap.class);
        int pageNO = Integer.parseInt(map.get("pageno").toString());
        int longNum = 50;
        map.put("rows", longNum );
        Option option =new Option();
        option.title("CPU utilization")
                .tooltip()
                .trigger(Trigger.axis)
                .axisPointer()
                .setType(PointerType.cross);

        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(Tool.saveAsImage);
        option.setToolbox(toolbox);
        option.grid().left(3).right(4).bottom(3).containLabel(true);
        List legendName = new ArrayList();

        HashMap retuen = new HashMap<>();
        List<String> listTime = taskService.queryAllCPUTime(map);
        if (listTime.size() < longNum) {
            map.put("pageno",0);
            retuen.put("pageno", 1);
        }
        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.boundaryGap(false);
        categoryAxis.setType(AxisType.category);
        categoryAxis.setData(listTime);
        option.xAxis(categoryAxis);
        option.yAxis(new ValueAxis().type(AxisType.value),new ValueAxis().type(AxisType.value));
        List temp2 = new ArrayList<>();
        for(int i=1;i<=7;i++){
            map.put("cid",i);
            Line line = new Line();
            HashMap series = new HashMap<>();
            List<Integer> list =  taskService.queryAllCPU(map);
            line.name("CPU"+i).type(SeriesType.line)
                    .stack("Total")
                    .setData(list);
            legendName.add("CPU"+i);
            series.put("name","CPU"+i);
            series.put("type","line");
            series.put("connectNulls", true);
            series.put("areaStyle","{}");
            series.put("stack","Total");
            series.put("data",list);
            temp2.add(series);


        }
        option.legend(legendName);
        retuen.put("legendT",legendName);
        retuen.put("xAxisT",listTime);
        retuen.put("seriesT",temp2);
        return AJAXReturn.success(retuen);
    }


    @GetMapping()
    public Object loadConsoleIndex(){
        return "index";
    }

    @ResponseBody
    @PostMapping("start/jobs")
    public Object loadDatabaseIndex(@RequestBody String jsonData){
        HashMap<String, Object> param = JSONObject.parseObject(jsonData, HashMap.class);
        int num = Integer.parseInt(String.valueOf(param.get("num")));
        Core core= new Core();
//        AJAXReturn ajaxReturn = core.STARTWORK(core,num);
//        if(ajaxReturn.getErrcode()==0){
//            String object = String.valueOf(ajaxReturn.getData());
//            HashMap job = JSONObject.parseObject(object, HashMap.class);
//            System.out.println(job);
//            taskService.insertJobs(job);
//        }
        return AJAXReturn.success();
    }



}
