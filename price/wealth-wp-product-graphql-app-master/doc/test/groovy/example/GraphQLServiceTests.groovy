package example


import com.dummy.wmd.wpc.graphql.client.GraphQLClient
import spock.lang.Shared
import spock.lang.Specification

class GraphQLServiceTests extends Specification {
    @Shared def client = new GraphQLClient("http://localhost:8080/graphql");
    def headerName = "X-dummy-E2E-TRUST-TOKEN"
    def headerValue = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImdpYy11YXQtZTJlIiwic2NoIjoidXJuOmFpbTp0b2tlbjppbnRlcm5hbCIsInNjdiI6IjEuMCIsInRrdiI6IjEuMCJ9.eyJqdGkiOiJkODA0MWFjOS04ZDViLTQ2ZmEtYmZiNy0wNTM1NTJlZWU2OTAiLCJpc3MiOiJ3ZWFsdGhTdGFmZkFwaUdhdGV3YXkiLCJpYXQiOjE2MTUyNTU5MTUsImV4cCI6MTYxNTI1NjAwOSwic2l0IjoiYWQ6dXNyOmVtcGxveWVlSWQiLCJzdWIiOiI0MzU5MTQyMSIsImdycCI6WyJDTj1JTkZPRElSLUhLLVdFQUxUSFNQTFVOSy1TUExVTktVU0VSLE9VPUhCQVAsT1U9U0ZXLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLVpPT00tUFJPLVRZUEUsT1U9QUFELE9VPUNMT1VEIFNFUlZJQ0VTLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQVdTLTcyNTc5MTM0MTkyMy1SRUFET05MWSxPVT1BV1MsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQVdTLTQ3OTQ2NTk2NTE1Ny1BUFBERVZPUFMsT1U9QVdTLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLVpPT00tUkVDT1JESU5HTE9DQUwtR1JPVVAsT1U9QUFELE9VPUNMT1VEIFNFUlZJQ0VTLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQVdTLTcwNzEwMDk5MDE4Ny1VU0FHRUJJTExJTkdWSUVXLE9VPUFXUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNjMyMjMzNTAzMDA2LUFQUERFVk9QUyxPVT1BV1MsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQVdTLTc1NTU5NDAyNDgyMS1BUFBERVZPUFMsT1U9QVdTLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLUpFTktJTlMtR0xXTS1VU0VSUyxPVT1KRU5LSU5TLE9VPURJR0lUQUxDSUNERU5HLE9VPUFBRCxPVT1DTE9VRCBTRVJWSUNFUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLUFXUy03MDcxMDA5OTAxODctTkVUV09SS0FETUlOLE9VPUFXUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNzEyMzE2NTg0NDI0LVJFQURPTkxZLE9VPUFXUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNzA3MTAwOTkwMTg3LURBVEFFTlJZUFQsT1U9QVdTLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLVNQTFVOS19ESUdJVEFMX0RFVl9HU1BfREVWRUxPUEVSLE9VPVNQTFVOSyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1EUy1ORVhVUy1VU0VSLE9VPUhCRVUsT1U9RFNORVhVUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNzA3MTAwOTkwMTg3LUFSQ0hJVEVDVFVSRVRFQU0sT1U9QVdTLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLURTLURJR0lUQUxKSVJBLVVTRVIsT1U9SEJFVSxPVT1ESUdJVEFMSklSQSxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNzEyMzE2NTg0NDI0LUFQUERFVk9QUyxPVT1BV1MsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItRlhUX1RPT0xJTkdfVVNFUlMsT1U9SEJFVSxPVT1HRlggVE9PTElORyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNzU1NTk0MDI0ODIxLVJFQURPTkxZLE9VPUFXUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BUFBTRUNUT09MUy1VU0VSUyxPVT1IQkVVLE9VPUFQUFNFQ1RPT0xTLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLURTLVNMQUNLLVVTRVIsT1U9U0xBQ0ssT1U9QUFELE9VPUNMT1VEIFNFUlZJQ0VTLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQUNBTk8tSEstVVNFUixPVT1BQ0FOTyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1EUy1ESUdJVEFMQ09ORkxVRU5DRS1VU0VSLE9VPUhCRVUsT1U9RElHSVRBTENPTkZMVUVOQ0UsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItRFMtTkVYVVMzLVVTRVIsT1U9SEJFVSxPVT1ORVhVUzMsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQUxNSkVOS0lOUy1VU0VSLE9VPUFMTUpFTktJTlMsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQVdTLTUxODEzMzk4OTkzMC1BUFBERVZPUFMsT1U9QVdTLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLURTLUFMTUdJVEhVQi1VU0VSLE9VPUhCRVUsT1U9QUxNR0lUSFVCLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLUFXUy03MDcxMDA5OTAxODctU0VSVkVSQURNSU4sT1U9QVdTLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLUFXUy03MDcxMDA5OTAxODctQ1JZUFRPLE9VPUFXUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1TUExVTktfRElHSVRBTF9QUk9EX0dTUF9ERVZFTE9QRVIsT1U9U1BMVU5LLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLURTLVNPTkFSLVVTRVIsT1U9SEJFVSxPVT1TT05BUixPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNzA3MTAwOTkwMTg3LUJJTExJTkdPV05FUixPVT1BV1MsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQVdTLTcwNzEwMDk5MDE4Ny1QQ0ZFTkdJTkVFUklORyxPVT1BV1MsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQUxNU05UTEMtVVNFUixPVT1IQkFQLE9VPUFMTVNOVExDLE9VPUFQUExJQ0FUSU9OUyxPVT1HUk9VUFMsREM9SU5GT0RJUixEQz1QUk9ELERDPUhTQkMiLCJDTj1JTkZPRElSLUpFTktJTlMtR0xXTS1QT1dFUlVTRVJTLE9VPUpFTktJTlMsT1U9RElHSVRBTENJQ0RFTkcsT1U9QUFELE9VPUNMT1VEIFNFUlZJQ0VTLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItUlRDLUpBWlpVU0VSUyxPVT1IQkVVLE9VPVJUQyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtMzY5NTMxMzMzMDg2LUFQUERFVk9QUyxPVT1BV1MsT1U9QVBQTElDQVRJT05TLE9VPUdST1VQUyxEQz1JTkZPRElSLERDPVBST0QsREM9SFNCQyIsIkNOPUlORk9ESVItQVdTLTcwNzEwMDk5MDE4Ny1ERVZPUFNURUFNLE9VPUFXUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNzA3MTAwOTkwMTg3LVJFQURPTkxZLE9VPUFXUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1IU0JDLldFQkVYLkNPTS1IT1NUQUNDT1VOVFMsT1U9V0VCRVgsT1U9QURGUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1BV1MtNzA3MTAwOTkwMTg3LURBVEFERUNSWVBULE9VPUFXUyxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIiwiQ049SU5GT0RJUi1TRVJWQ08tU04tU0MtQUNDRVNTLE9VPVNFUlZDTyBTRVJWSUNFIENBVEFMT0dVRSxPVT1BUFBMSUNBVElPTlMsT1U9R1JPVVBTLERDPUlORk9ESVIsREM9UFJPRCxEQz1IU0JDIl19.HIDtwhoTrt4MLR01AIa1NI7CzfAUGfAXsfaBSZo9ycv5xrfMGyZe9smkPMk_uiTDYrWfuLKqaouFyI0saSCPRidoTSH5dgMRnapHhQhrRaTKd-wkAvl1YZoQGLy5dT3ur_NNSTke9QamA7XoH8B0MKsMYpaOg2q_FfaMso7XroeQsVqRRQ9sX_C4CNORDoknpNGJF0SaGqsgCmFV6Uay5UewaLAsULZcG4WxxyQhU6MUmMSo42zZZjaelMci3UnffFr8yKzcpsp5K8jSRI1tTtTXm031Ydjm9y_Q5JEeB_eqg4yvSKCz6iwBGB58nIKM8SGgtJe-BYAGOlZLCUoDLg=="

    def 'user info'() {
        def query = """
            query userInfo {
              userInfo {
                id
                name
                roles
              }
            }
        """
        def expected = [data: [userInfo: [id: '43591421', name: null, roles: []]]]
        client.header(headerName, headerValue)

        when:
        def resp = client.post(query)

        then:
        assert resp
    }


    def 'test amendmentByFilter'(String recStatCde,String actionCde,String filters) {
        given:
        def query = """
            query amendmentByFilter{
              amendmentByFilter(filter: $filters) {
                recStatCde
                actionCde
              }
            }
        """
        def expected = [data: [amendmentByFilter: [[recStatCde: recStatCde, actionCde: actionCde]]]]
        client.header(headerName, headerValue)

        when:
        def resp = client.post(query)

        then:
        println(resp)
        resp == expected

        where:
        recStatCde | actionCde | filters
        'draft'    | 'add'     | '{_id: 2204}'
        'pending'  | 'update'  | '{_id: 2204}'

    }


    def 'test amendmentCreate'() {
        given:
        def mutation = """
            mutation amdCreate{
              amendmentCreate(actionCde: $actionCde, docType: $doyType, docChanged: $docChanged) {
                _id
                recStatCde
              }
            }     
        """
        def expected = [data: [amendmentCreate: [[_id: 2200, recStatCde: "$recStatCde"]]]]
        client.header(headerName, headerValue)

        when:
        def resp = client.post(mutation)

        then:
        println(resp)
        resp == expected

        where:
        actionCde | recStatCde | doyType          | docChanged
        'add'     | 'draft'    | 'reference_data' | '{ctryRecCde: "GB",grpMembrRecCde: "HBEU",cdvTypeCde: "ASSETCATECDE",cdvCde: "FIN",cdvDesc: "Fixed Income",cdvPllDesc: "Fixed Income",cdvSllDesc: null,cdvDispSeqNum: 0,cdvParntTypeCde: null,cdvParntCde: null,recCmntText: null}'
        'update'  | 'draft'    | 'reference_data' | '{_id: 2151, revision: 1,ctryRecCde: "GB",grpMembrRecCde: "HBEU",cdvTypeCde: "ASSETCATECDE",cdvCde: "FIN",cdvDesc: "Fixed Income",cdvPllDesc: "Fixed Income",cdvSllDesc: null,cdvDispSeqNum: 0,cdvParntTypeCde: null,cdvParntCde: null,recCmntText: null}'
        'delete'  | 'pending'  | 'reference_data' | '{_id: 1, revision: 1,ctryRecCde: "GB",grpMembrRecCde: "HBEU",cdvTypeCde: "ASSETCATECDE",cdvCde: "FIN",cdvDesc: "Fixed Income",cdvPllDesc: "Fixed Income",cdvSllDesc: null,cdvDispSeqNum: 0,cdvParntTypeCde: null,cdvParntCde: null,recCmntText: null}'
    }


    def 'test amendmentUpdate'() {
        given:
        def mutation = """
            mutation amdUpdate{
              amendmentUpdate(amendmentId: $amendmentId, docChanged: $docChanged) {
                recStatCde
                actionCde
                docType
              }
            }    
        """
        def expected = [data: [amendmentUpdate:[recStatCde:"$recStatCde", actionCde:"$actionCde", docType:"$docType"]]]
        client.header(headerName, headerValue)

        expect:
        def resp = client.post(mutation)
        resp == expected

        where:
        amendmentId | recStatCde | actionCde | docType          | docChanged
        2213        | 'pending'  | 'update'  | 'reference_data' | '{_id: 1,revision: 1,ctryRecCde: "GB",grpMembrRecCde: "HBEU",cdvTypeCde: "ASSETCATECDE",cdvCde: "FIN",cdvDesc: "Fixed Income",cdvPllDesc: "Fixed Income",cdvSllDesc: null,cdvDispSeqNum: 0,cdvParntTypeCde: null,cdvParntCde: null,recCmntText: null}'
    }


    def 'test amendmentRequestApproval'() {
        given:
        def mutation = """
            mutation requestApprove {
              amendmentRequestApproval(amendmentId: $amendmentId, comments: $comments) {
                recStatCde
              } 
            }
        """
        def expected = [data: [amendmentRequestApproval: [recStatCde: 'pending']]]
        client.header(headerName, headerValue)

        when:
        def resp = client.post(mutation)

        then:
        resp == expected

        where:
        amendmentId | comments
        2214        | '"approve me pls"'
    }


    def 'test amendmentApprove'() {
        given:
        def mutation = """
            mutation amendmentApprove {
              amendmentApprove(amendmentId: $amendmentId, approvalAction: $approvalAction, comments: $comments) {
                recStatCde
                approvalComment
              }
            }
        """
        def expected = [data: [amendmentApprove: [recStatCde: 'approved',approvalComment: 'it has been approved']]]
        client.header(headerName, headerValue)

        when:
        def resp = client.post(mutation)

        then:
        resp == expected

        where:
        amendmentId | approvalAction | comments
        2205        | "approved"     | '"it has been approved"'
        2213        | "rejected"     | '"it has been rejected"'
        2215        | "returned"     | '"it has been returned"'
    }

}

