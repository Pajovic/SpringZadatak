package organization;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

import organization.model.Employee;
import organization.model.Organization;
import organization.model.OrganizationData;
import organization.model.OrganizationExternalAccess;
import organization.model.OrganizationExternalAccess.EmployeeRight;
import organization.service.EmployeeService;
import organization.service.OrganizationService;
import organization.wrappers.EmployeeDataWrapper;
import organization.wrappers.EmployeeInternalRightsWrapper;
import organization.wrappers.OrganizationExternalAccessWrapper;

@SpringBootApplication(scanBasePackages={"organization"})
public class OrganizationStartupApp implements CommandLineRunner {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(OrganizationStartupApp.class, args);	

	}

	@Override
	public void run(String... arg0) throws Exception {

		mongoTemplate.getDb().dropDatabase();
		
		Organization org = new Organization(1,"B");		
		organizationService.AddNewOrganization(org);
		org = new Organization(2,"C");
		organizationService.AddNewOrganization(org);
		
		Employee emp = new Employee(1,1,"Pera","Peric");				
		employeeService.AddNewEmployee(emp);
		emp = new Employee(2,1,"Mika","Mikic");			
		employeeService.AddNewEmployee(emp);
		emp = new Employee(3,1,"Zora","Zoric");		

		employeeService.AddNewEmployee(emp);
		
		emp = new Employee(1,2,"Sloba","Stankovic");		
		emp.setCreateRight(true);
		emp.setReadRight(true);
		emp.setUpdateRight(true);
		emp.setDeleteRight(true);		
		employeeService.AddNewEmployee(emp);
		emp = new Employee(2,2,"Jovana","Jovanovic");		
	
		employeeService.AddNewEmployee(emp);
		emp = new Employee(3,2,"Nemanja","Nemanjic");		
	
		employeeService.AddNewEmployee(emp);
		EmployeeInternalRightsWrapper eirw = new EmployeeInternalRightsWrapper(1,1,true,true,true,true);
		organizationService.ModifyEmployeeRights(eirw);
		
		eirw = new EmployeeInternalRightsWrapper(2,1,false,true,false,false);
		organizationService.ModifyEmployeeRights(eirw);
		
		eirw = new EmployeeInternalRightsWrapper(3,1,false,true,false,false);
		organizationService.ModifyEmployeeRights(eirw);
		
		eirw = new EmployeeInternalRightsWrapper(1,2,true,true,true,true);
		organizationService.ModifyEmployeeRights(eirw);
		
		eirw = new EmployeeInternalRightsWrapper(2,2,true,true,true,false);
		organizationService.ModifyEmployeeRights(eirw);
		
		eirw = new EmployeeInternalRightsWrapper(3,2,false,true,false,false);
		organizationService.ModifyEmployeeRights(eirw);
				
		EmployeeDataWrapper edw = new EmployeeDataWrapper(1,1,1,"Elektromotor", 5, 550);
		employeeService.AddNewData(edw);
		edw = new EmployeeDataWrapper(1,2,1,"Energetski kabel", 8, 100);
		employeeService.AddNewData(edw);
		edw = new EmployeeDataWrapper(1,3,1,"Osigurac", 120, 20);
		employeeService.AddNewData(edw);
		edw = new EmployeeDataWrapper(1,4,1,"Kuciste", 20, 200);
		employeeService.AddNewData(edw);
		
		edw = new EmployeeDataWrapper(1,1,2,"Ves masina", 5, 2000);
		employeeService.AddNewData(edw);
		edw = new EmployeeDataWrapper(1,2,2,"Bojler", 4, 500);
		employeeService.AddNewData(edw);
		edw = new EmployeeDataWrapper(1,3,2,"Sporet", 2, 1200);
		employeeService.AddNewData(edw);
		edw = new EmployeeDataWrapper(1,4,2,"Sudo masina", 7, 800);
		employeeService.AddNewData(edw);
		HashSet<Integer> organizationIds = new HashSet<Integer>();
		organizationIds.add(2);
		
		String criteria = String.format(" amount :{$lt : 10} , organizationId : %d ",1);
		OrganizationExternalAccess oea = new OrganizationExternalAccess(1,1, criteria,EmployeeRight.R,organizationIds);
		organizationService.AddExternalAccess(oea);
		
		criteria = String.format(" amount :{$gt : 10} , organizationId : %d ",1);
		oea = new OrganizationExternalAccess(2,1, criteria,EmployeeRight.R,organizationIds);
		organizationService.AddExternalAccess(oea);
		
		criteria = String.format(" amount :{$gt : 10} , organizationId : %d ",1);
		oea = new OrganizationExternalAccess(3,1, criteria,EmployeeRight.U,organizationIds);
		organizationService.AddExternalAccess(oea);
		
		criteria = String.format(" amount :{$gt : 10} , organizationId : %d ",1);
		oea = new OrganizationExternalAccess(4,1, criteria,EmployeeRight.D,organizationIds);
		organizationService.AddExternalAccess(oea);
		
		
		OrganizationExternalAccessWrapper oeaw = new OrganizationExternalAccessWrapper("1_1", true);		
		organizationService.ModifyExternalAccess(oeaw);
			
		oeaw = new OrganizationExternalAccessWrapper("2_1", true);		
		organizationService.ModifyExternalAccess(oeaw);
		
		oeaw = new OrganizationExternalAccessWrapper("3_1", true);		
		organizationService.ModifyExternalAccess(oeaw);
		
		oeaw = new OrganizationExternalAccessWrapper("4_1", true);
		organizationService.ModifyExternalAccess(oeaw);		
		
	}
}








