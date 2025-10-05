package amendments.reference_data

import com.dummy.wmd.wpc.graphql.client.GraphQLClient
import spock.lang.Shared
import spock.lang.Specification

/**
 * Test reference_data amendment.
 */
class CreateUpdateTests extends Specification {
    @Shared
    def client = new GraphQLClient("http://localhost:8080/graphql");
    def ANSI_RED = "\u001B[31m"
    def ANSI_RESET = "\u001B[0m"
    /**
     * Test update a reference_data item, and then approve it
     *
     * Steps:
     *     1. amendmentCreate(update)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(returned) with the amendmentId generated in (1)
     *     4. amendmentUpdate with the amendmentId generated in (1)
     *     5. amendmentRequestApproval with the amendmentId generated in (1)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. referenceDataByFilter expect found with field changed, and revision++
     */
    def 'approve happy flow'() {
        given:
        def expected = referenceDataByFilter("{_id: $id}")
        def revisionStr = expected.get("data").getAt("referenceDataByFilter").getAt("revision").toString()
        def revision = java.lang.Integer.valueOf(revisionStr.substring(1, revisionStr.length() - 1))
        def amendmentCreateResp = amendmentCreate("update", "reference_data", "{revision: $revision,$docChanged}")
        def amendmentId = amendmentCreateResp.get("data").getAt("amendmentCreate").getAt("_id")
        amendmentRequestApproval(amendmentId, requestComments)
        amendmentApprove(amendmentId, approvalAction, approveComments)
        amendmentUpdate(amendmentId, "{revision: $revision,$docChanged2}")
        amendmentRequestApproval(amendmentId, requestComments)
        amendmentApprove(amendmentId, approvalAction2, approveComments2)
        def resp = referenceDataByFilter("{_id: $id}")
        def newRevisionStr = resp.get("data").getAt("referenceDataByFilter").getAt("revision").toString()
        def newRevision = java.lang.Integer.valueOf(newRevisionStr.substring(1, newRevisionStr.length() - 1))

        when:
        println("amendmentId is " + amendmentId)
        println("newRevision is " + newRevision)

        then:
        resp != expected
        newRevision == revision + 1

        where:
        description          | requestComments    | approvalAction | approveComments          | approvalAction2 | approveComments2         | date                       | id   | docChanged                                    | docChanged2
        'update and approve' | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | String.valueOf(new Date()) | 2152 | '_id: ' + id + ',' + read("docChangedUpdate") | '_id: ' + id + ',cdvPllDesc: "Fixed Income' + date + '",' + read("docChangedUpdate2")

    }

    /**
     * Test update a reference_data item, and then reject it
     *
     * Steps:
     *     1. amendmentCreate(update)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(returned) with the amendmentId generated in (1)
     *     4. amendmentUpdate with the amendmentId generated in (1)
     *     5. amendmentRequestApproval with the amendmentId generated in (1)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. referenceDataByFilter expect found with field not changed, and revision no change
     */
    def 'reject happy flow'() {
        def amendmentId
        given:
        def expected = referenceDataByFilter("{_id: $id}")
        def revisionStr = expected.get("data").getAt("referenceDataByFilter").getAt("revision").toString()
        def revision = revisionStr.substring(1, revisionStr.length() - 1)
        def amendmentCreateResp = amendmentCreate("update", "reference_data", "{revision: $revision,$docChanged}")
        if (amendmentCreateResp.containsKey("errors")) {
            println(ANSI_RED + amendmentCreateResp + ANSI_RESET)
        } else {
            amendmentId = amendmentCreateResp.get("data").getAt("amendmentCreate").getAt("_id")
            def amendmentRequestResp = amendmentRequestApproval(amendmentId, requestComments)
            if (amendmentRequestResp.containsKey("errors")) {
                println(ANSI_RED + new Exception("cdvParntTypeCde + cdvParntCde should exist,and key combination (ctryRecCde+grpMembrRecCde+cdvTypeCde+cdvCde) should not change") + ANSI_RESET)
                amendmentDelete(amendmentId)
            } else {
                amendmentApprove(amendmentId, approvalAction, approveComments)
                amendmentUpdate(amendmentId, "{revision: $revision,$docChanged2}")
                def amendmentRequest2Resp = amendmentRequestApproval(amendmentId, requestComments)
                println(ANSI_RED + amendmentRequest2Resp + ANSI_RESET)
                amendmentApprove(amendmentId, approvalAction2, approveComments2)
            }
        }
        def resp = referenceDataByFilter("{_id: $id}")
        def newRevisionStr = resp.get("data").getAt("referenceDataByFilter").getAt("revision").toString()
        def newRevision = newRevisionStr.substring(1, newRevisionStr.length() - 1)

        when:
        println("newRevision is " + newRevision)

        then: 'expect found with field not changed, and revision no change'
        resp == expected
        newRevision == revision

        where:
        description                 | requestComments    | approvalAction | approveComments          | approvalAction2 | approveComments2         | date                       | id   | docChanged                                                                  | docChanged2
        'on going amendment'        | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | String.valueOf(new Date()) | 2153 | '_id: ' + id + ',' + read("docChangedUpdate")                               | '_id: ' + id + ',cdvPllDesc: "Fixed Income' + date + '",' + read("docChangedUpdate2")
        //'cdvParntTypeCde not exist' | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | String.valueOf(new Date()) | 10   | '_id: ' + id + ',' + read("noCdvParntTypeCdeUpdate")                        | '_id: ' + id + ',' + read("noCdvParntTypeCdeUpdate")
        //'cdvParntCde not exist'     | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | String.valueOf(new Date()) | 10   | '_id: ' + id + ',' + read("noCdvParntCdeUpdate")                            | '_id: ' + id + ',' + read("noCdvParntCdeUpdate")
        //'key combination changed'   | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | String.valueOf(new Date()) | 10   | '_id: ' + id + ',cdvTypeCde: "ASSETCATECDE' + date + '",' + read("changed") | '_id: ' + id + ',' + read("changed")

    }

    String read(String key) {
        Properties p = new Properties();
        String value = "";
        def fis
        try {
            fis = new FileInputStream(new File("src/test/groovy/amendments/reference_data/referenceData.properties"));
            p.load(fis);
            value = p.getProperty(key);
        } catch (Exception e) {
            println(e.getAt("message"))
        } finally {
            if (fis != null) {
                fis.close()
            }
        }
        return value;
    }

    Map<String, Object> amendmentCreate(String actionCde, String docType, String docChanged) {
        def amendmentCreate = """
            mutation {
              amendmentCreate(actionCde: $actionCde, docType: $docType, docChanged: $docChanged) {
                _id
              }
            }     
        """
        return client.post(amendmentCreate)
    }

    Map<String, Object> amendmentRequestApproval(int amendmentId, String requestComments) {
        def amendmentRequestApproval = """
            mutation {
              amendmentRequestApproval(amendmentId: $amendmentId, comments: $requestComments) {   
                 recStatCde            
              } 
            }
        """
        return client.post(amendmentRequestApproval)
    }

    Map<String, Object> amendmentApprove(int amendmentId, String approvalAction, String approveComments) {
        def amendmentApprove = """
            mutation {
              amendmentApprove(amendmentId: $amendmentId, approvalAction: $approvalAction, comments: $approveComments) {
                 recStatCde
              }
            }
        """
        return client.post(amendmentApprove)
    }

    Map<String, Object> amendmentUpdate(int amendmentId, String docChanged2) {
        def amendmentUpdate = """
            mutation {
               amendmentUpdate(amendmentId: $amendmentId, docChanged: $docChanged2) {
                 recStatCde
               }
            }
        """
        return client.post(amendmentUpdate)
    }

    Map<String, Object> amendmentDelete(int amendmentId) {
        def amendmentDelete = """
                    mutation {
                        amendmentDelete(amendmentId: $amendmentId){
                            recStatCde
                        }
                    }
                """
        return client.post(amendmentDelete)
    }

    Map<String, Object> referenceDataByFilter(String docChanged2) {
        def referenceDataByFilter = """
            query {
                referenceDataByFilter(filter:$docChanged2){
                    _id
                    revision
                }
            }
        """
        return client.post(referenceDataByFilter)
    }
}

