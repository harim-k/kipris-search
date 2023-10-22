package com.example.kiprissearch.external;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class KiprisClientTest {

    private final String partialMatchPattern = "<title>.*?%s.*?</title>";
    private final String test2 = "<title>길쭉이단어장</title>";
    String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<response>\n" +
            "    <header>\n" +
            "        <requestMsgID></requestMsgID>\n" +
            "        <responseTime>2023-09-12 02:37:38.3738</responseTime>\n" +
            "        <responseMsgID></responseMsgID>\n" +
            "        <successYN>Y</successYN>\n" +
            "        <resultCode>00</resultCode>\n" +
            "        <resultMsg>NORMAL SERVICE.</resultMsg>\n" +
            "    </header>\n" +
            "    <body>\n" +
            "        <items>\n" +
            "            <item>\n" +
            "                <agentName></agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>주식회사 아발론교육</applicantName>\n" +
            "                <applicationDate>20160321</applicationDate>\n" +
            "                <applicationNumber>4020160020628</applicationNumber>\n" +
            "                <applicationStatus>등록</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ad7a17eeeef6e4ea4b5e22ef00dd3e29d6c998e272153d94f2d0d313099593c856e2f8b1b19c8f016877519d7d651165</bigDrawing>\n" +
            "                <classificationCode>09</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a9134423c9c66155bdee31073a17e90b32061e0f2f082a640dc1a61628a54a11e9ca92</drawing>\n" +
            "                <fullText>Y</fullText>\n" +
            "                <indexNo>1</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate>20160908</publicationDate>\n" +
            "                <publicationNumber>4020160093702</publicationNumber>\n" +
            "                <regPrivilegeName>주식회사 아발론교육</regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate>20161215</registrationDate>\n" +
            "                <registrationNumber>4012217350000</registrationNumber>\n" +
            "                <registrationPublicDate>20161221</registrationPublicDate>\n" +
            "                <registrationPublicNumber>4020160034519</registrationPublicNumber>\n" +
            "                <title>내 손안에 단어장 아보카도</title>\n" +
            "                <viennaCode></viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName>윤제국|황보의</agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>윤인하</applicantName>\n" +
            "                <applicationDate>20210208</applicationDate>\n" +
            "                <applicationNumber>4020210027071</applicationNumber>\n" +
            "                <applicationStatus>등록</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ad7a17eeeef6e4ea4b5e22ef00dd3e2940f80548c1358af3c7e461abec2174b13e6e1f4bc58b208d84a29da83f6951d253af3cecf453907a</bigDrawing>\n" +
            "                <classificationCode>16</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a91344ae7b1568aff8c767c4e151061e6197e2901d2a897da36f374787e384405981176943e361ee5ef86e</drawing>\n" +
            "                <fullText>Y</fullText>\n" +
            "                <indexNo>2</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate>20210407</publicationDate>\n" +
            "                <publicationNumber>4020210046268</publicationNumber>\n" +
            "                <regPrivilegeName>윤인하 Yoon, Inha</regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate>20210804</registrationDate>\n" +
            "                <registrationNumber>4017597460000</registrationNumber>\n" +
            "                <registrationPublicDate>20210809</registrationPublicDate>\n" +
            "                <registrationPublicNumber>4020210083207</registrationPublicNumber>\n" +
            "                <title>길쭉이단어장</title>\n" +
            "                <viennaCode></viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName></agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>스톰스터디 주식회사</applicantName>\n" +
            "                <applicationDate>20220907</applicationDate>\n" +
            "                <applicationNumber>4020220165992</applicationNumber>\n" +
            "                <applicationStatus>출원</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ad7a17eeeef6e4ea4b5e22ef00dd3e2942dee2feafab2a09b475c69b0414bbee32107ba8c8cbcd54f80508a1194dd0c2877cccaf4409e3c2</bigDrawing>\n" +
            "                <classificationCode>41</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a913442817c88e040e84f7802f019e69eba5b23dac5f2fb346cad2a189f8ac72f43eab095da3a4efb251a5</drawing>\n" +
            "                <fullText>N</fullText>\n" +
            "                <indexNo>3</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate></publicationDate>\n" +
            "                <publicationNumber></publicationNumber>\n" +
            "                <regPrivilegeName></regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate></registrationDate>\n" +
            "                <registrationNumber></registrationNumber>\n" +
            "                <registrationPublicDate></registrationPublicDate>\n" +
            "                <registrationPublicNumber></registrationPublicNumber>\n" +
            "                <title>스톰단어장</title>\n" +
            "                <viennaCode></viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName>이성록|전상구</agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>박수연</applicantName>\n" +
            "                <applicationDate>20090520</applicationDate>\n" +
            "                <applicationNumber>4020090023177</applicationNumber>\n" +
            "                <applicationStatus>거절</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=75a63eed5a438998fe897a23675ce4c37cef60776c026cf22b6d5078fbabbb97b7690996ee7e5b8452351d2a2922cae1</bigDrawing>\n" +
            "                <classificationCode>09</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a91344caa2520f8fd62e9077968b85fa3bb5212ec76ffbc07a5bb1a327f7a5ee64b1cb</drawing>\n" +
            "                <fullText>N</fullText>\n" +
            "                <indexNo>4</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate></publicationDate>\n" +
            "                <publicationNumber></publicationNumber>\n" +
            "                <regPrivilegeName></regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate></registrationDate>\n" +
            "                <registrationNumber></registrationNumber>\n" +
            "                <registrationPublicDate></registrationPublicDate>\n" +
            "                <registrationPublicNumber></registrationPublicNumber>\n" +
            "                <title>말하는단어장</title>\n" +
            "                <viennaCode>040521|260414|261109</viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName>특허법인태동</agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>김정훈|강정식</applicantName>\n" +
            "                <applicationDate>20090303</applicationDate>\n" +
            "                <applicationNumber>4020090009538</applicationNumber>\n" +
            "                <applicationStatus>거절</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=75a63eed5a438998fe897a23675ce4c37cef60776c026cf22752f8d718229b46effe6e084aa27b899bb74f13a98ad379</bigDrawing>\n" +
            "                <classificationCode>09|28</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a91344caa2520f8fd62e90e4434638141873aa5d895bdbc41657f4471b9e15bdc5a113</drawing>\n" +
            "                <fullText>N</fullText>\n" +
            "                <indexNo>5</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate></publicationDate>\n" +
            "                <publicationNumber></publicationNumber>\n" +
            "                <regPrivilegeName></regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate></registrationDate>\n" +
            "                <registrationNumber></registrationNumber>\n" +
            "                <registrationPublicDate></registrationPublicDate>\n" +
            "                <registrationPublicNumber></registrationPublicNumber>\n" +
            "                <title>똑똑한 영어단어장</title>\n" +
            "                <viennaCode></viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName></agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>(주)유니크맥스</applicantName>\n" +
            "                <applicationDate>20120717</applicationDate>\n" +
            "                <applicationNumber>4020120045544</applicationNumber>\n" +
            "                <applicationStatus>포기</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=75a63eed5a438998fe897a23675ce4c3cbcae00bd7eb6ab6ab83e9a4dadb4ecaec13950787b4146ab5444999fa904a68</bigDrawing>\n" +
            "                <classificationCode>16</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a91344bb3a21d1402d2ea448de9036d398440d2df14b334487331f8a7321beaabafffe</drawing>\n" +
            "                <fullText>Y</fullText>\n" +
            "                <indexNo>6</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate>20130312</publicationDate>\n" +
            "                <publicationNumber>4020130023478</publicationNumber>\n" +
            "                <regPrivilegeName></regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate></registrationDate>\n" +
            "                <registrationNumber></registrationNumber>\n" +
            "                <registrationPublicDate></registrationPublicDate>\n" +
            "                <registrationPublicNumber></registrationPublicNumber>\n" +
            "                <title>뻔뻔한단어장</title>\n" +
            "                <viennaCode></viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName></agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>오의남</applicantName>\n" +
            "                <applicationDate>20190429</applicationDate>\n" +
            "                <applicationNumber>4020190067057</applicationNumber>\n" +
            "                <applicationStatus>거절</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ad7a17eeeef6e4ea4b5e22ef00dd3e293e70a322c3ead7b624546f8d671b30f0cae6c46304e46696ad069b8f25a7ee119e843c54071508d8</bigDrawing>\n" +
            "                <classificationCode>09</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a9134435594e3384b42e76b74a4ee07b777ed95b421968608b14b379375b97276ef0dd4bcb80512d553f6d</drawing>\n" +
            "                <fullText>N</fullText>\n" +
            "                <indexNo>7</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate></publicationDate>\n" +
            "                <publicationNumber></publicationNumber>\n" +
            "                <regPrivilegeName></regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate></registrationDate>\n" +
            "                <registrationNumber></registrationNumber>\n" +
            "                <registrationPublicDate></registrationPublicDate>\n" +
            "                <registrationPublicNumber></registrationPublicNumber>\n" +
            "                <title>내단어장뷰어</title>\n" +
            "                <viennaCode></viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName>특허법인영비</agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>신승아</applicantName>\n" +
            "                <applicationDate>20230710</applicationDate>\n" +
            "                <applicationNumber>4020230122213</applicationNumber>\n" +
            "                <applicationStatus>출원</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ad7a17eeeef6e4ea4b5e22ef00dd3e2991ba5d9d09407d4b84fb0908160c5ef4d5119222b366cdcc368226938971a306288338dd728c6878</bigDrawing>\n" +
            "                <classificationCode>16</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a913440be47db3b03c4b738ccf5466d9ce5a9ad1ae1c7c227ec8b066a8fc8d66dae8a618c71e67cfcbf17d</drawing>\n" +
            "                <fullText>N</fullText>\n" +
            "                <indexNo>8</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate></publicationDate>\n" +
            "                <publicationNumber></publicationNumber>\n" +
            "                <regPrivilegeName></regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate></registrationDate>\n" +
            "                <registrationNumber></registrationNumber>\n" +
            "                <registrationPublicDate></registrationPublicDate>\n" +
            "                <registrationPublicNumber></registrationPublicNumber>\n" +
            "                <title>셀라키즈</title>\n" +
            "                <viennaCode></viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName></agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>중앙데일리 주식회사</applicantName>\n" +
            "                <applicationDate>20220426</applicationDate>\n" +
            "                <applicationNumber>4020220078042</applicationNumber>\n" +
            "                <applicationStatus>공고</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ad7a17eeeef6e4ea4b5e22ef00dd3e2942dee2feafab2a0985cab4539fdc386f3147c89260ddfa6f588a24add545b8e8b82047e7d6b9d705</bigDrawing>\n" +
            "                <classificationCode>16|25|35|41</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a913442817c88e040e84f74276fa3a5e318ac4b7ee3d263b1b296abe79c87ea85219a388c1834c93ba354d</drawing>\n" +
            "                <fullText>Y</fullText>\n" +
            "                <indexNo>9</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate>20230907</publicationDate>\n" +
            "                <publicationNumber>4020230158282</publicationNumber>\n" +
            "                <regPrivilegeName></regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate></registrationDate>\n" +
            "                <registrationNumber></registrationNumber>\n" +
            "                <registrationPublicDate></registrationPublicDate>\n" +
            "                <registrationPublicNumber></registrationPublicNumber>\n" +
            "                <title></title>\n" +
            "                <viennaCode>040501|040505|050520</viennaCode>\n" +
            "            </item>\n" +
            "            <item>\n" +
            "                <agentName>유성원</agentName>\n" +
            "                <appReferenceNumber></appReferenceNumber>\n" +
            "                <applicantName>정철</applicantName>\n" +
            "                <applicationDate>20211221</applicationDate>\n" +
            "                <applicationNumber>4020210259097</applicationNumber>\n" +
            "                <applicationStatus>등록</applicationStatus>\n" +
            "                <bigDrawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ad7a17eeeef6e4ea4b5e22ef00dd3e2940f80548c1358af37799d59be39232d802b9d5a2e8e015d7236259832593f7063be32f223a1ab099</bigDrawing>\n" +
            "                <classificationCode>16</classificationCode>\n" +
            "                <drawing>http://plus.kipris.or.kr/kiprisplusws/fileToss.jsp?arg=ed43a0609e94d6e251697a9d72a91344ae7b1568aff8c7679fbaa8586e02b431e8094439381157566ff2ca95fb360dab632b4c4b29183908</drawing>\n" +
            "                <fullText>Y</fullText>\n" +
            "                <indexNo>10</indexNo>\n" +
            "                <internationalRegisterDate></internationalRegisterDate>\n" +
            "                <internationalRegisterNumber></internationalRegisterNumber>\n" +
            "                <priorityDate></priorityDate>\n" +
            "                <priorityNumber></priorityNumber>\n" +
            "                <publicationDate>20230524</publicationDate>\n" +
            "                <publicationNumber>4020230092927</publicationNumber>\n" +
            "                <regPrivilegeName>정철 JUNG, CHUL</regPrivilegeName>\n" +
            "                <regReferenceNumber></regReferenceNumber>\n" +
            "                <registrationDate>20230904</registrationDate>\n" +
            "                <registrationNumber>4020773130000</registrationNumber>\n" +
            "                <registrationPublicDate>20230907</registrationPublicDate>\n" +
            "                <registrationPublicNumber>4020230123995</registrationPublicNumber>\n" +
            "                <title>JC어학원</title>\n" +
            "                <viennaCode>260218|260224|270910</viennaCode>\n" +
            "            </item>\n" +
            "        </items>\n" +
            "    </body>\n" +
            "    <count>\n" +
            "        <numOfRows>10</numOfRows>\n" +
            "        <pageNo>1</pageNo>\n" +
            "        <totalCount>482</totalCount>\n" +
            "    </count>\n" +
            "</response>";

    @Test
    void name() {
        Pattern pattern = Pattern.compile(test2);
//        System.out.println(String.format(partialMatchPattern, "단어장"));
        if (pattern.matcher(responseBody).matches()) {
            System.out.println("####");
        } else {
            System.out.println("$$$$");

        }
    }
}