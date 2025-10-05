package amendments.reference_data

import com.dummy.wmd.wpc.graphql.client.GraphQLClient
import spock.lang.Shared
import spock.lang.Specification

/**
 * Test reference_data amendment.
 */
class CreateAddTests extends Specification {
    @Shared
    def client = new GraphQLClient("http://localhost:8080/graphql");
    def ANSI_RED = "\u001B[31m"
    def ANSI_RESET = "\u001B[0m"

    /**
     * Test create new reference_data item and approve it
     *
     * Steps:
     *     1. amendmentCreate(add)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(returned) with the amendmentId generated in (1)
     *     4. amendmentUpdate with the amendmentId generated in (1)
     *     5. amendmentRequestApproval with the amendmentId generated in (1)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. referenceDataByFilter to find the new created reference_data, expect found
     */
    def 'approve happy flow'() {
        given:
        def amendmentCreateResp = amendmentCreate(actionCde, docType, docChanged)
        def amendmentId = amendmentCreateResp.get("data").getAt("amendmentCreate").getAt("_id")
        amendmentRequestApproval(amendmentId, requestComments)
        amendmentApprove(amendmentId, approvalAction, approveComments)
        amendmentUpdate(amendmentId, docChanged2)
        amendmentRequestApproval(amendmentId, requestComments)
        amendmentApprove(amendmentId, approvalAction2, approveComments2)
        def resp = referenceDataByFilter(docChanged2)
        def referenceDataId = resp.get("data").getAt("referenceDataByFilter").getAt("_id")

        when:
        println("amendmentId is " + amendmentId)
        println("referenceDataId is " + referenceDataId)

        then:
        resp.isEmpty() == false

        where:
        description                  | requestComments    | approvalAction | approveComments          | approvalAction2 | approveComments2         | actionCde | docType          | date                       | docChanged                                                            | docChanged2
        'returned and request again' | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | 'add'     | 'reference_data' | String.valueOf(new Date()) | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged") + '}' | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged2") + '}'
        //'rejected and request again' | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | 'add'     | 'reference_data' | String.valueOf(new Date()) | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged") + '}' | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged2") + '}'

    }


    /**
     * Test create new reference_data item and reject it
     *
     * Steps:
     *     1. amendmentCreate(add)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(returned) with the amendmentId generated in (1)
     *     4. amendmentUpdate with the amendmentId generated in (1)
     *     5. amendmentRequestApproval with the amendmentId generated in (1)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. referenceDataByFilter to find the new created reference_data, expect not found
     */
    def 'reject happy flow'() {
        given:
        def createResp = amendmentCreate(actionCde, docType, docChanged)
        if (createResp.containsKey("errors")) {
            println(ANSI_RED + createResp.get("errors").getAt("message") + ANSI_RESET)
        } else {
            def amendmentId = createResp.get("data").getAt("amendmentCreate").getAt("_id")
            println("amendmentId is " + amendmentId)
            def requestResp = amendmentRequestApproval(amendmentId, requestComments)
            if (requestResp.containsKey("errors")) {
                println(ANSI_RED + new Exception("cdvParntTypeCde + cdvParntCde should exist,and key combination (ctryRecCde+grpMembrRecCde+cdvTypeCde+cdvCde) should not repeat") + ANSI_RESET)
            }
            amendmentApprove(amendmentId, approvalAction, approveComments)
            amendmentUpdate(amendmentId, docChanged2)
            def requestResp2 = amendmentRequestApproval(amendmentId, requestComments)
            if (requestResp2.containsKey("errors")) {
                println(ANSI_RED + new Exception("cdvParntTypeCde + cdvParntCde should exist,and key combination (ctryRecCde+grpMembrRecCde+cdvTypeCde+cdvCde) should not repeat") + ANSI_RESET)
            }
            amendmentApprove(amendmentId, approvalAction2, approveComments2)
        }
        def resp = referenceDataByFilter(docChanged2)

        when:
        def expectedNotFind = [data: [referenceDataByFilter: []]]

        then:
        resp == expectedNotFind

        where:
        description                 | requestComments    | approvalAction | approveComments          | approvalAction2 | approveComments2         | actionCde | docType          | date                       | docChanged                                                                   | docChanged2
        'incorrect actionCde'       | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | 'ad'      | 'reference_data' | String.valueOf(new Date()) | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged") + '}'        | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged2") + '}'
        //'incorrect docType'         | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | 'add'     | 'reference'      | String.valueOf(new Date()) | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged") + '}'        | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("docChanged2") + '}'
        //'cdvParntTypeCde not exist' | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | 'add'     | 'reference_data' | String.valueOf(new Date()) | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("noCdvParntTypeCde") + '}' | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("noCdvParntTypeCde") + '}'
        //'cdvParntCde not exist'     | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | 'add'     | 'reference_data' | String.valueOf(new Date()) | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("noCdvParntCde") + '}'     | '{cdvTypeCde: "ASSETCATECDE' + date + '",' + read("noCdvParntCde") + '}'
        //'key combination repeated'  | '"approve me pls"' | "returned"     | '"it has been returned"' | "approved"      | '"it has been approved"' | 'add'     | 'reference_data' | String.valueOf(new Date()) | read("repeated")                                                             | read("repeated")

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

