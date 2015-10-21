import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.mo.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Servlet implementation class GetVM
 */
@WebServlet("/GetVM")
public class VMControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public VMControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			System.out.println("VM PAge");
			long start = System.currentTimeMillis();
			URL url = new URL("https://130.65.132.115/sdk");
			ServiceInstance si = new ServiceInstance(url, "administrator", "12!@qwQW", true);
			long end = System.currentTimeMillis();
			System.out.println("time taken:" + (end-start));
			Folder rootFolder = si.getRootFolder();
			String name = rootFolder.getName();
			ArrayList<String> VMNameList = new ArrayList<String>();
			ArrayList<String> VMOSList = new ArrayList<String>();
			ArrayList<String> VMIPList = new ArrayList<String>();
			ArrayList<String> VMPowerStateList = new ArrayList<String>();
			
			System.out.println("root:" + name);
			ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
			HttpSession session = request.getSession(true);	
			 session.setAttribute("VMCount",mes.length); 
			if(mes==null || mes.length ==0)
			{
				return;
			}
			
			for(int  i=0;i<mes.length;i++)
			{
				System.out.println(i);
			VirtualMachine vm = (VirtualMachine) mes[i]; 
			
			VirtualMachineConfigInfo vminfo = vm.getConfig();
			VirtualMachineCapability vmc = vm.getCapability();

			vm.getResourcePool();
			session = request.getSession(true);	
			VMNameList.add(vm.getName());
			VMOSList.add(vminfo.getGuestFullName());
			VMIPList.add(vm.getGuest().ipAddress);
			VMPowerStateList.add((vm.getRuntime().getPowerState()).toString());
			
	        //  System.out.println(i);
			System.out.println("Hello " + vm.getName());
			System.out.println("GuestOS: " + vminfo.getGuestFullName());
			System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());
			System.out.println("VMPowerStateList: " + (vm.getRuntime().getPowerState()).toString());
			}
			
			session.setAttribute("currentVMName",VMNameList); 
			System.out.println("currentVMName " + VMNameList);
	         session.setAttribute("currentGuestName",VMOSList); 
	         session.setAttribute("currentVMIp",VMIPList); 
	         session.setAttribute("currentVMPowerState",VMPowerStateList); 
			si.getServerConnection().logout();
			
			response.sendRedirect("/CMPE283_-_Project_2/VMInfo.jsp"); //error page 
		}
		
		catch(Exception e)
		{
			System.out.println("Exc " + e);
			
		}
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
