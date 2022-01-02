/*
 *
 *
 *    Copyright (c) 2021.  Virtualan Contributors (https://virtualan.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *     in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software distributed under the License
 *     is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *     or implied. See the License for the specific language governing permissions and limitations under
 *     the License.
 *
 *
 *
 */

package io.virtualan.cucumblan.standard;

import io.virtualan.idaithalam.core.generator.ExcelToCollectionGenerator;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class EDI270And271Parser implements StandardProcessing {

  static Map<String, String> elementMap = new HashMap();
  static Map<String, String> providerMap = new HashMap();

  private static String getEDI271AsJson(String content) {
    String[] contents = content.split("~");
    AtomicInteger i = new AtomicInteger(0);
    List<JSONObject> list = Arrays.stream(contents)
        .map(x -> buildJsonObject(x, i.incrementAndGet())).collect(
            Collectors.toList());
    JSONArray array = new JSONArray();
    list.stream().forEach(x -> array.put(x));
    return array.toString();
  }

  public static JSONObject buildJsonObject(String content, int index) {
    String[] contents = content.trim().split("\\*");
    JSONObject newObject = new JSONObject();
    initialize();
    String code = elementMap.get(contents[0]);
    if (code == null && contents.length > 1) {
      code = elementMap.get(contents[0] + "*" + contents[1]);
    }
    if (contents[0] != null && !contents[0].isEmpty()) {
      if (code == null) {
        newObject.put(contents[0], content.trim());
      } else {
        String[] codes = code.split("\\*");
        JSONObject childObject = new JSONObject();

        List<JSONObject> lists = IntStream
            .range(0, contents.length)
            .filter(i -> !codes[i].isEmpty())
            .mapToObj(i -> {
              childObject.put(codes[i], contents[i]);
              return childObject;
            }).collect(Collectors.toList());
        newObject.put(contents[0] + "::" + index, childObject);
      }
    }
    return newObject;
  }

  public static void initialize() {
    elementMap.put("ST",
        "Code*TransactionSet*TransactionSetIDCode*TransactionSetControlNumber*Implementation Convention Reference = 005010X279A1");
    elementMap.put("BHT",
        "Code*HierarchicalStructureCode*TransactionSetPurposeCode*ReferenceIdentification*Date*Time");
    elementMap.put("HL",
        "Code*HierarchicalIDNumber*HierarchicalParentIDNumber*HierarchicalLevelCode*HierarchicalChildCode");
    elementMap.put("NM1",
        "Code*EntityIdentifierCode*EntityTypeQualifier*LastName*FirstName*MiddleName*NamePrefix*NameSuffix*IdentificationCodeQualifier*IdentificationCode");
    elementMap.put("TRN",
        "Code*TraceTypeCode*ReferenceIdentification*OriginatingCompanyIdentifier*ReferenceIdentification");
    elementMap.put("N3", "Code*AddressInformation1*AddressInformation2");
    elementMap.put("N4", "Code*City*StateorProvCode*PostalCode");
    elementMap.put("DMG", "Code*DateTimePeriodFormat*DateTimePeriod*GenderCode");
    elementMap.put("DTP", "Code*DateTimeQualifier*DateTimePeriodFormatQualifier*DateTimePeriod");
    elementMap.put("EB*1",
        "Code*EligibilityorBenefitInformationCode*CoverageLevelCode*ServiceTypeCode*InsuranceTypeCode*PlanCoverageDescription");
    elementMap.put("EB*B",
        "EligibilityorBenefitInformationCode*CoverageLevelCode*MedicalCareServiceTypeCode*ChiropracticServiceTypeCode*DentalCareServiceTypeCode*HospitalServiceTypeCode*EmergencyServicesServiceTypeCode*PharmacyServiceTypeCode*ProfessionalPhysicianVisitOfficeServiceTypeCode*VisionOptometryServiceTypeCode*MentalHealthServiceTypeCode*UrgentCareServiceTypeCode*InsuranceTypeCode*PlanCoverageDescription*TimePeriodQualifier*MonetaryValue*Percent*QuantityQualifier*Quantity*CertificationAuthorizationIndicator*InPlanNetworkIndicator");
    elementMap.put("LS", "Code*LoopIdentifierCode");
    elementMap.put("LE", "Code*LoopIdentifierCode");
    elementMap.put("SE", "Code*NumberofIncludedSegments*TransactionSetControlNumber");
    elementMap
        .put("AAA", "Code*ValidityCode*AgencyQualifierCode*RejectReasonCode*FollowUpActionCode");
  }

  public static void init() {
    providerMap.put("1", "Medical Care");
    providerMap.put("33", "Chiropractic");
    providerMap.put("35", "Dental Care");
    providerMap.put("47", "Hospital");
    providerMap.put("86", "Emergency Services");
    providerMap.put("88", "Pharmacy");
    providerMap.put("98", "Professional (Physician) Visit – Office");
    providerMap.put("AL", "Vision (Optometry)");
    providerMap.put("MH", "Mental Health");
    providerMap.put("UC", "Urgent Care");
  }

  public static void main(String[] args) throws Exception {

    new EDI270And271Parser().getXMLValue(ExcelToCollectionGenerator.getFileAsString(
        "\"D:\\\\Elan\\\\virtualan-software-ws\\\\idaithalam\\\\excel-idaithalam\\\\idaithalam\\\\samples\\\\idaithalam-excel-apitesting\\\\src\\\\test\\\\resources",
        "output.xml"));
  }

  @Override
  public String getType() {
    return "EDI-271";
  }

  @Override
  public String preRequestProcessing(String s) {
    return null;
  }

  @Override
  public String postResponseProcessing(String s) {
    return getEDI271AsJson(getXMLValue(s));
    //return getEDI271AsJson(s);
  }

  private String getXMLValue(String xml) {
    String response = null;
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    try {
      InputSource ips = new InputSource(new StringReader(xml));
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document xmlDocument = builder.parse(ips);
      XPath xPath = XPathFactory.newInstance().newXPath();
      String expression = "//request/text()";
      NodeList nodeList = (NodeList) xPath.compile(expression)
          .evaluate(xmlDocument, XPathConstants.NODESET);
      response = nodeList.item(0).getNodeValue();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

}
