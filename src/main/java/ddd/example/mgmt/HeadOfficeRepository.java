package ddd.example.mgmt;

public interface HeadOfficeRepository {

	HeadOffice findById(Long id);

	HeadOffice save(HeadOffice headOffice);
	
}
