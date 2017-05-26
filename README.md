# RESTFul-API-Drill

This API uses the Spring Boot Framework to implement RESTFul-API that supports uploading files filled with metadata to an in memory database using an hslqdb and jpa repository. The database contents is also written to the file system after every new upload. This implementation also supports the ability to download the database content stream as a text file in JSON format. This API gives the user the ability to create, update, get a single entry, get all entries, as well as delete single entries from the persistent database.



***Get all entries*** - GET "/api/metadata"

***Get single entry*** - GET "/api/metadata/{id}"

***Create entry*** - POST "/api/metadata"

***Update entry*** - PUT "/api/metadata/{id}"

***Delete entry*** - DELETE "/api/metadata/{id}"


***Upload file form*** - GET "/"

***Uploading file*** - POST "/"

***Download Content Stream*** - GET "/getallfiles"

***Get specific file*** - GET "/files/{filesname:.+}"
