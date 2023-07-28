# JOPA Multitenant Proof of Concept

This project is a proof of concept of using declarative Spring transactions with JOPA in a multitenant application.

The application will access dynamically created repositories resolved based on tenant identifier stored
in session.

## Example

See the [example.postman_collection.json](./example.postman_collection.json) Postman collection for an example of using the demo. The only necessary thing
is to create the following repositories:
- `http://localhost:18080/rdf4j-server/flagship`
- `http://localhost:18080/rdf4j-server/saas-tenant-one`
- `http://localhost:18080/rdf4j-server/saas-tenant-two`

And then run the collection.
