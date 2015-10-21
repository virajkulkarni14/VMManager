import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;

/**
 * Servlet implementation class CreateNewVM
 */

public class VMManagerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public VMManagerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        try {

            URL url = new URL("https://130.65.132.115/sdk");
            ServiceInstance si = new ServiceInstance(url, "administrator", "12!@qwQW", true);

            Folder rootFolder;
            ResourcePool rp;
            Datacenter dc;
            ManagedEntity[] hosts;
            ManagedEntity[] vms;


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

            long memorySizeMB = 500;
            int cupCount = 1;
            String guestOsId = "sles10Guest";
            long diskSizeKB = 1000000;
            // mode: persistent|independent_persistent,
            // independent_nonpersistent
            String diskMode = "persistent";
            String datastoreName = "nfs3team15";
            String netName = "VM Network";
            String nicName = "Network Adapter 1";

            HttpSession session = request.getSession(true);
            // create vm config spec
            VirtualMachineConfigSpec vmSpec =
                    new VirtualMachineConfigSpec();
            UserModel currentUser = (UserModel) (session.getAttribute("currentSessionUser"));
            currentUser.setVMName(request.getParameter("vm-Name"));

            //String vmName = request.getAttribute("vm-name");
            vmSpec.setName(currentUser.getVMName());
            vmSpec.setAnnotation("VirtualMachine Annotation");
            vmSpec.setMemoryMB(memorySizeMB);
            vmSpec.setNumCPUs(cupCount);
            vmSpec.setGuestId(guestOsId);

            // create virtual devices
            int cKey = 1000;
            VirtualDeviceConfigSpec scsiSpec = createScsiSpec(cKey);
            VirtualDeviceConfigSpec diskSpec = createDiskSpec(
                    datastoreName, cKey, diskSizeKB, diskMode);
            VirtualDeviceConfigSpec nicSpec = createNicSpec(
                    netName, nicName);

            vmSpec.setDeviceChange(new VirtualDeviceConfigSpec[]
                    {scsiSpec, diskSpec, nicSpec});

            // create vm file info for the vmx file
            VirtualMachineFileInfo vmfi = new VirtualMachineFileInfo();
            vmfi.setVmPathName("[" + datastoreName + "]");
            vmSpec.setFiles(vmfi);

            // call the createVM_Task method on the vm folder
            Task task = vmFolder.createVM_Task(vmSpec, rp, null);
            String result = task.waitForMe();
            if (result == Task.SUCCESS) {
                System.out.println("VM Created Sucessfully");
            } else {
                System.out.println("VM could not be created. ");
            }


            response.sendRedirect("/CMPE283_-_Project_2/CreateVMJsp.jsp");
        } catch (Exception e) {
            System.out.println("Exception New VM " + e);
        }
    }


    static VirtualDeviceConfigSpec createScsiSpec(int cKey) {
        VirtualDeviceConfigSpec scsiSpec =
                new VirtualDeviceConfigSpec();
        scsiSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
        VirtualLsiLogicController scsiCtrl =
                new VirtualLsiLogicController();
        scsiCtrl.setKey(cKey);
        scsiCtrl.setBusNumber(0);
        scsiCtrl.setSharedBus(VirtualSCSISharing.noSharing);
        scsiSpec.setDevice(scsiCtrl);
        return scsiSpec;
    }

    static VirtualDeviceConfigSpec createDiskSpec(String dsName,
                                                  int cKey, long diskSizeKB, String diskMode) {
        VirtualDeviceConfigSpec diskSpec =
                new VirtualDeviceConfigSpec();
        diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
        diskSpec.setFileOperation(
                VirtualDeviceConfigSpecFileOperation.create);

        VirtualDisk vd = new VirtualDisk();
        vd.setCapacityInKB(diskSizeKB);
        diskSpec.setDevice(vd);
        vd.setKey(0);
        vd.setUnitNumber(0);
        vd.setControllerKey(cKey);

        VirtualDiskFlatVer2BackingInfo diskfileBacking =
                new VirtualDiskFlatVer2BackingInfo();
        String fileName = "[" + dsName + "]";
        diskfileBacking.setFileName(fileName);
        diskfileBacking.setDiskMode(diskMode);
        diskfileBacking.setThinProvisioned(true);
        vd.setBacking(diskfileBacking);
        return diskSpec;
    }

    static VirtualDeviceConfigSpec createNicSpec(String netName,
                                                 String nicName) throws Exception {
        VirtualDeviceConfigSpec nicSpec =
                new VirtualDeviceConfigSpec();
        nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);

        VirtualEthernetCard nic = new VirtualPCNet32();
        VirtualEthernetCardNetworkBackingInfo nicBacking =
                new VirtualEthernetCardNetworkBackingInfo();
        nicBacking.setDeviceName(netName);

        Description info = new Description();
        info.setLabel(nicName);
        info.setSummary(netName);
        nic.setDeviceInfo(info);

        // type: "generated", "manual", "assigned" by VC
        nic.setAddressType("generated");
        nic.setBacking(nicBacking);
        nic.setKey(0);

        nicSpec.setDevice(nic);
        return nicSpec;
    }


    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
