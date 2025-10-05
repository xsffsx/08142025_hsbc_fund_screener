package amendments.reference_data

import com.dummy.wmd.wpc.graphql.client.GraphQLClient
import spock.lang.Shared
import spock.lang.Specification

/**
 * Test reference_data amendment.
 */
class CreateDeleteTests extends Specification {
    @Shared
    def client = new GraphQLClient("http://localhost:8080/graphql");
    def ANSI_RED = "\u001B[31m"
    def ANSI_RESET = "\u001B[0m"
    /**
     * Test delete a reference_data item, and then approve it
     * Steps:
     *     1. amendmentCreate(add)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(approved) with the amendmentId generated in (1)
     *     4. referenceDataByFilter to find the deleted item in reference_data
     *     5. amendmentCreate(delete)
     *     6. amendmentApprove(approved) with the amendmentId generated in (4)
     *     7. referenceDataByFilter to find the deleted item in reference_data, expect not found
     */
    def 'approve happy flow'() {
        given:
        def amendmentCreateResp = amendmentCreate("add","reference_data",docChanged)
        def amendmentId = amendmentCreateResp.get("data").getAt("amendmentCreate").getAt("_id")
        amendmentRequestApproval(amendmentId,requestComments)
        amendmentApprove(amendmentId,approvalAction,approveComments)
        def referenceDataResp = referenceDataByFilter(docChanged)
        def referenceDataIdStr = referenceDataResp.get("data").getAt("referenceDataByFilter").getAt("_id").toString()
        def referenceDataId = referenceDataIdStr.substring(1, referenceDataIdStr.length() - 1)
        def referenceDataRevisionStr = referenceDataResp.get("data").getAt("referenceDataByFilter").getAt("revision").toString()
        def referenceDataRevision = referenceDataRevisionStr.substring(1, referenceDataRevisionStr.length() - 1)
        def amendmentCreateResp2 = amendmentCreate("delete","reference_data","{_id: $referenceDataId,revision: $referenceDataRevision,$docChanged2}")
        println(amendmentCreateResp2)
        def amendmentId2 = amendmentCreateResp2.get("data").getAt("amendmentCreate").getAt("_id")
        def amendmentApprove2Resp = amendmentApprove(amendmentId2,approvalAction,approveComments)
        println(ANSI_RED + amendmentApprove2Resp + ANSI_RESET)
        def resp = referenceDataByFilter("{_id: $referenceDataId}")

        when:
        println("amendmentId is " + amendmentId)
        println("amendmentId2 is " + amendmentId2)
        println("referenceDataId is " + referenceDataId)

        then:
        resp.isEmpty() == true

        where:
        description              | requestComments    | approvalAction | approveComments          | date                       | docChanged                                                            | docChanged2
        'added and deleted data' | '"approve me pls"' | "approved"     | '"it has been approved"' | String.valueOf(new Date()) | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged") + '}' | 'cdvTypeCde: "ASSETCATECDE '+ date + '",' + read("docChanged")

    }

    /**
     * Test delete a reference_data item, and then reject it
     * Steps:
     *     1. amendmentCreate(add)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(approved) with the amendmentId generated in (1)
     *     4. referenceDataByFilter to find the deleted item in reference_data
     *     5. amendmentCreate(delete)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. referenceDataByFilter to find the deleted item in reference_data, expect found
     */
    def 'reject happy flow'() {
        given:
        def amendmentCreateResp = amendmentCreate("add","reference_data",docChanged)
        def amendmentId = amendmentCreateResp.get("data").getAt("amendmentCreate").getAt("_id")
        amendmentRequestApproval(amendmentId,requestComments)
        amendmentApprove(amendmentId,approvalAction,approveComments)
        def  referenceDataResp = referenceDataByFilter(docChanged)
        def referenceDataIdStr = referenceDataResp.get("data").getAt("referenceDataByFilter").getAt("_id").toString()
        def referenceDataId = referenceDataIdStr.substring(1, referenceDataIdStr.length() - 1)
        def referenceDataRevisionStr = referenceDataResp.get("data").getAt("referenceDataByFilter").getAt("revision").toString()
        def referenceDataRevision = referenceDataRevisionStr.substring(1, referenceDataRevisionStr.length() - 1)
        def amendmentCreate2Resp = amendmentCreate("delete","reference_data","{revision: $referenceDataRevision,$docChanged2}")
        def amendmentId2
        if (amendmentCreate2Resp.containsKey("errors")) {
            println(ANSI_RED + new Exception("id or revision are not exist") + ANSI_RESET)
        } else {
            amendmentId2 = amendmentCreate2Resp.get("data").getAt("amendmentCreate").getAt("_id")
            amendmentApprove(amendmentId2,approvalAction,approveComments)
        }
        def resp = referenceDataByFilter("{_id: $referenceDataId}")

        when:
        println("amendmentId is " + amendmentId)
        println("referenceDataId is " + referenceDataId)

        then:
        resp.isEmpty() == false

        where:
        description                    | requestComments    | approvalAction | approveComments          | date                       | docChanged                                                            | docChanged2
        'id or revision are not exist' | '"approve me pls"' | "approved"     | '"it has been approved"' | String.valueOf(new Date()) | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged") + '}' | 'cdvTypeCde: "ASSETCATECDE '+ date + '",' + read("docChanged")

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
    Map<String, Object> amendmentApprove(int amendmentId, String approvalAction,String approveComments) {
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

