import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;


/**
 * Servlet implementation class DeployVM
 */

public class DeployVMServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public DeployVMServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        try {
            System.out.println("Deploying VM ");


            URL url = new URL("https://130.65.132.115/sdk");
            ServiceInstance si = new ServiceInstance(url, "administrator", "12!@qwQW", true);

            Folder rootFolder;
            ResourcePool rp;
            Datacenter dc;
            ManagedEntity[] hosts;
            ManagedEntity[] vms;
            VirtualMachine vm = null;


            ServiceInstance vCenterManagerSi;
            Folder vCenterManagerRootFolder;
            String dcName = "T15-DC";
            Folder vmFolder;


            System.out.println("Hello New CreateVM");

            rootFolder = si.getRootFolder();
            dc = (Datacenter) new InventoryNavigator(
                    rootFolder).searchManagedEntity("Datacenter", dcName);
            rp = (ResourcePool) new InventoryNavigator(
                    dc).searchManagedEntities("ResourcePool")[0];

            vmFolder = dc.getVmFolder();


            VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
            cloneSpec.setLocation(new VirtualMachineRelocateSpec());
            cloneSpec.setPowerOn(true);
            cloneSpec.setTemplate(false);


            String OSType = request.getParameter("choiceOS");
            if ("Windows".equals(OSType)) {
                vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", "T15-VM01-Windows");
            }
            if ("Ubuntu".equals(OSType)) {
                vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", "T15-VM02-Ubu");
            }
            Task task = vm.cloneVM_Task((Folder) vm.getParent(), (request.getParameter("vm-deployName")), cloneSpec);
            System.out.println("Launching the VM clone task. " + "Please wait ...");

            String status = task.waitForMe();
            System.out.println(status);
            if (status == Task.SUCCESS) {
                System.out.println("VM got cloned successfully.");
            } else {
                System.out.println("Failure -: VM cannot be cloned");
            }
        } catch (Exception e) {
            System.out.println("Exception in deploying VM " + e);
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
