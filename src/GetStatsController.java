import com.vmware.vim25.*;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


@WebServlet("/VMstatistics")
public class GetStatsController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static ArrayList<String> co = new ArrayList<String>();
    private static ArrayList<String> cpuyco = new ArrayList<String>();
    private static ArrayList<String> memoryyco = new ArrayList<String>();
    private static HttpSession session;
    private static ArrayList<Date> date = new ArrayList<Date>();
    private static ArrayList<String> cpuPerformance = new ArrayList<String>();

    private static ArrayList<String> CPUStatInfoMap = new ArrayList<String>();
    private static ArrayList<String> memoryStatInfoMap = new ArrayList<String>();
    private static ArrayList<ArrayList<String>> statInfo = new ArrayList<ArrayList<String>>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("YYY,MM,dd");

    /**
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public GetStatsController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    VirtualMachine vm;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        session = request.getSession(true);

        try {


            URL url = new URL("https://130.65.132.115/sdk");
            ServiceInstance si = new ServiceInstance(url, "administrator", "12!@qwQW", true);

            String vmname = "T15-VM01-Windows";
            vm = (VirtualMachine) new InventoryNavigator(
                    si.getRootFolder()).searchManagedEntity(
                    "VirtualMachine", vmname);

            if (vm == null) {
                System.out.println("Virtual Machine " + vmname
                        + " cannot be found.");
                si.getServerConnection().logout();
                return;
            }

            //	System.out.println("CPU Statistics");

            PerformanceManager perfMgr = si.getPerformanceManager();

            int perfInterval = 86400; // 30 minutes for PastWeek

            PerfMetricId[] pmis = perfMgr.queryAvailablePerfMetric(vm, null, null, perfInterval);
            PerfCounterInfo[] o = perfMgr.getPerfCounter();
            Map<String, Integer> perfCounterInfoMap = new HashMap<String, Integer>();
            for (int k = 0; k < o.length; k++) {
                perfCounterInfoMap.put(o[k].getNameInfo().key, o[k].getKey());
            }
            //-->>>>>>>>>
            for (PerfMetricId id : pmis) {
                id.setInstance("");
            }
            //-->>>>>>>>>


            Calendar curTime = si.currentTime();

            PerfQuerySpec qSpec = new PerfQuerySpec();
            qSpec.setEntity(vm.getRuntime().getHost());
            //metricIDs must be provided, or InvalidArgumentFault
            qSpec.setMetricId(pmis);
            qSpec.setFormat("normal"); //optional since it's default
            qSpec.setIntervalId(perfInterval);

            Calendar startTime = (Calendar) curTime.clone();
            startTime.roll(Calendar.DATE, -10);
            System.out.println("start:" + startTime.getTime());
            qSpec.setStartTime(startTime);

            Calendar endTime = (Calendar) curTime.clone();
            endTime.roll(Calendar.DATE, 0);
            System.out.println("end:" + endTime.getTime());
            qSpec.setEndTime(endTime);

            PerfCompositeMetric pv = perfMgr.queryPerfComposite(qSpec);
            if (pv != null) {
                printPerfMetric(pv.getEntity());
                PerfEntityMetricBase[] pembs = pv.getChildEntity();
                for (int i = 0; pembs != null && i < pembs.length; i++) {
                    System.out.println("zxc: " + i);
                    System.out.println("Pens: " + pembs.length);
                    printPerfMetric(pembs[i]);
                }
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("xTime", co);
            session.setAttribute("yCPU", cpuyco);

            si.getServerConnection().logout();
            response.sendRedirect("/CMPE283_-_Project_2/CPUStatisticsJsp.jsp");
        } catch (Exception e) {
            System.out.println("CPU Statistics Exception " + e);
        }
    }

    void printPerfMetric(PerfEntityMetricBase val) {
        String entityDesc = val.getEntity().getType() + ":" + val.getEntity().get_value();
        System.out.println("Entity:" + entityDesc);
        if (val instanceof PerfEntityMetric) {
            printPerfMetric((PerfEntityMetric) val);
        } else if (val instanceof PerfEntityMetricCSV) {
            printPerfMetricCSV((PerfEntityMetricCSV) val);
        } else {
            System.out.println("UnExpected sub-type of " + "PerfEntityMetricBase.");
        }
    }

    void printPerfMetric(PerfEntityMetric pem) {
        PerfMetricSeries[] vals = pem.getValue();
        PerfSampleInfo[] infos = pem.getSampleInfo();

        long[] statValue;
        System.out.println("Vals " + vals.length + " infos " + infos.length);
        for (int i = 0; vals != null && i < vals.length; i++) {
            if (vals[i].getId().getCounterId() == 2) {
                System.out.println("CPU statistics >>>>>>>>>>>>>>>>>>>");
                if (vals[i] instanceof PerfMetricIntSeries) {
                    PerfMetricIntSeries val = (PerfMetricIntSeries) vals[i];
                    statValue = val.getValue();
                    for (int k = 0; k < statValue.length - 1; k++) {
                        CPUStatInfoMap.add("[new Date(" + sdf.format(infos[k].getTimestamp().getTime()).toString() + "), " + statValue[k] + "],");
                        //System.out.println("[new Date("+sdf.format(infos[k].getTimestamp().getTime()).toString()+"), "+statValue[k]+"]");
                        //		CPUStatInfoMap.add(sdf.format(infos[k].getTimestamp().getTime()).toString()+statValue[k]);
                        co.add(sdf.format(infos[statValue.length - 1].getTimestamp().getTime()).toString());
                        cpuyco.add(String.valueOf(statValue[statValue.length - 1]));
                    }

                    //	System.out.println("[new Date("+sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+") , "+statValue[statValue.length-1]+"]");
                }
            }


            //System.out.println("CPU Cout:" + CPUStatInfoMap.size());
            if (vals[i].getId().getCounterId() == 24) {
                System.out.println("Memory statistics >>>>>>>>>>>>>>>>>>>");
                if (vals[i] instanceof PerfMetricIntSeries) {
                    PerfMetricIntSeries val = (PerfMetricIntSeries) vals[i];
                    statValue = val.getValue();
                    for (int k = 0; k < statValue.length - 1 && k < 10; k++) {
                        memoryStatInfoMap.add("[new Date(" + sdf.format(infos[k].getTimestamp().getTime()).toString() + "), " + statValue[k] + "],");
                        //System.out.println("[new Date("+sdf.format(infos[k].getTimestamp().getTime()).toString()+") , "+statValue[k]+"],");
                        //memoryStatInfoMap.put(sdf.format(infos[k].getTimestamp().getTime()).toString(), statValue[k]);
                    }
                    //	memoryStatInfoMap.add("[new Date("+sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+"), "+statValue[statValue.length-1]+"]");
                    //System.out.println("[new Date("+sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+") , "+statValue[statValue.length-1]+"]");
                }
            }

            //System.out.println("Mem Cout:" + memoryStatInfoMap.size());
        }
    
    /*	System.out.println("CPU Statistics: ");
        for(Map.Entry<String, Long> entry: CPUStatInfoMap.entrySet()){
    		System.out.println(entry.getKey()+" "+entry.getValue());
    	}
    	System.out.println("Memory Statistics");
    	for(Map.Entry<String, Long> entry: memoryStatInfoMap.entrySet()){
    		System.out.println(entry.getKey()+" "+entry.getValue());
    	}
    */
        statInfo.add(CPUStatInfoMap);
        statInfo.add(memoryStatInfoMap);
//    	Object[] statInfoAll = (Object[]) new Object();
//    	statInfoAll.
    }


    void printPerfMetricCSV(PerfEntityMetricCSV pems) {
        System.out.println("SampleInfoCSV:" + pems.getSampleInfoCSV());
        PerfMetricSeriesCSV[] csvs = pems.getValue();
        for (int i = 0; i < csvs.length; i++) {
            System.out.println("PerfCounterId:" + csvs[i].getId().getCounterId());
            System.out.println("CSV sample values:" + csvs[i].getValue());
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
