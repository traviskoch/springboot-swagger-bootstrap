# DataAdapter Admin Console Service
This is the backend service for an admin console to install and maintain DataAdapters on-prem or cloud-based.

### Rest Endpoints
All REST endpoints use 'dynamic' path. This means that path ``**`` is used as relative path in *base directory*.  
* __GET__ http://localhost:8888/services/files/list/** - list content directory or subdirectory  
  ``curl -X GET http://localhost:8888/services/files/list/``
* __GET__ http://localhost:8888/services/files/download/** - download file on path. file must exist.   
  ``curl -X GET http://localhost:8888/services/files/list/path/to/001-data.txt``

#### Upload files
* __POST__ http://localhost:8888/services/files/upload/** - upload file, parent directory(ies) must exist before upload  
 ``curl -F 'file=@/local/path/to/file.txt' http://localhost:8888/services/files/upload/path/to/001-data.txt``

#### Delete files and directories
* __DELETE__ http://localhost:8888/services/files/delete/** - delete file or directory  
  ``curl -X DELETE http://localhost:8888/services/files/delete/path/to/001-data.txt``

#### Create empty directory
* __POST__ http://localhost:8888/services/files/createdir/** - create empty directory  
  ``curl -X POST http://localhost:8888/services/files/createdir/path/to/directory``

### Build and run
Variable ``file.server.home`` in ``application.properties`` file defines *base directory* to be exposed via REST APIs.
```
mvn clean install
java -jar target/dataadapter-service-1.0.0-SNAPSHOT.jar --spring.config.location=file:./src/main/resources/application.properties
```
