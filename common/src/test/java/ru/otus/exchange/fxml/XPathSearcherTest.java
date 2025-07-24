package ru.otus.exchange.fxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.exchange.fxml.XPathSearcher.joinStackToString;
import static ru.otus.exchange.fxml.XPathSearcher.joinStringStack;
import static ru.otus.exchange.fxml.XPathSearcher.splitPathIntoParentAndChild;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.xml.namespace.QName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class XPathSearcherTest {

    @Test
    void testJoinStringStack() {
        Deque<String> list = new LinkedList<>() {
            {
                add("a");
                add("b");
            }
        };

        assertEquals("a/b", joinStringStack(list));
    }

    @Test
    void testGetValuesByPath0() throws Exception {

        for (int i = 0; i <= 1; i++) {
            try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/xml" + i + ".xml")) {
                byte[] xml = new byte[Objects.requireNonNull(in).available()];
                Assertions.assertEquals(xml.length, in.read(xml));

                String searchPath1 = joinStringStack(new LinkedList<>() {
                    {
                        add("a");
                        add("b");
                    }
                });

                String searchPath2 = joinStringStack(new LinkedList<>() {
                    {
                        add("a");
                        add("c");
                    }
                });

                List<String> qnamePathList = new LinkedList<>() {
                    {
                        add(searchPath1);
                        add("a");
                        add(searchPath2);
                    }
                };

                XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));

                Map<String, String> resultMap = searcher.getValuesByPath(qnamePathList);
                assertEquals("привет мир", resultMap.get(searchPath1));

                assertTrue(
                        new LinkedList<String>() {
                            {
                                add("c");
                                add("b");
                            }
                        }.contains(resultMap.get("a")));
                assertEquals("\n\n\n    ", resultMap.get(searchPath2));
            }
        }
    }

    @Test
    void testGetValuesByPath1() throws Exception {

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/child_anket_yaml_4t_f.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            String lifecycleIdPath = joinStringStack(new LinkedList<>() {
                {
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").toString());
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Header").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "Addressing").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "LifecycleInfo").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "LifecycleID").toString());
                }
            });

            String messageIdPath = joinStringStack(new LinkedList<>() {
                {
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").toString());
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Header").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "Addressing").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "MessageID").toString());
                }
            });

            String messageTypePath = joinStringStack(new LinkedList<>() {
                {
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").toString());
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Body").toString());
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Form").toString());
                }
            });

            List<String> qnamePathList = new LinkedList<>() {
                {
                    add(lifecycleIdPath);
                    add(messageIdPath);
                    add(messageTypePath);
                }
            };

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));

            Map<String, String> resultMap = searcher.getValuesByPath(qnamePathList);
            assertEquals("1000230012018100900000008", resultMap.get(lifecycleIdPath));
            assertEquals("1000230012018100900000110", resultMap.get(messageIdPath));
            assertEquals("{http://xsd.gspvd/v001/forms/form}TravelDocumentChildYaml", resultMap.get(messageTypePath));
        }
    }

    @Test
    void testGetValuesByPath2() throws Exception {

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/goznak_fail.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            String faultstringPath1 = joinStringStack(new LinkedList<>() {
                {
                    push(new QName("http://xsd.gspvd/v001/utility/faults", "Faultstring").toString());
                }
            });

            String faultstringPath2 = joinStringStack(new LinkedList<>() {
                {
                    push(new QName("http://xsd.gspvd/v001/utility/faults/", "Faultstring").toString());
                }
            });

            List<String> qnamePathList = new LinkedList<>() {
                {
                    add(faultstringPath1);
                    add(faultstringPath2);
                }
            };

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));

            Map<String, String> resultMap = searcher.getValuesByPath(qnamePathList);
            assertEquals(
                    "&#x41F;&#x43E;&#x432;&#x442;&#x43E;&#x440;&#x43D;&#x43E;&#x435; &#x437;&#x430;&#x434;&#x430;&#x43D;&#x438;&#x435; &#x43D;&#x430; &#x43F;&#x435;&#x447;&#x430;&#x442;&#x44C; &#x441; MessageId 9000000032019043052150923 &#x431;&#x443;&#x434;&#x435;&#x442; &#x43F;&#x440;&#x43E;&#x438;&#x433;&#x43D;&#x43E;&#x440;&#x438;&#x440;&#x43E;&#x432;&#x430;&#x43D;&#x43E;.",
                    resultMap.get(faultstringPath1));
            assertNull(resultMap.get(faultstringPath2));
        }
    }

    @Test
    void testGetValuesByPathNotXml0() throws IOException {

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/not_xml0.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            assertThrows(NotXmlException.class, () -> searcher.getValuesByPath(new LinkedList<>()));
        }
    }

    @Test
    void testGetValuesByPathNotXml2() throws IOException {

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/not_xml2.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            assertThrows(NotXmlException.class, () -> searcher.getValuesByPath(new LinkedList<>()));
        }
    }

    @Test
    void testGetValuesByPathNotXml3() throws IOException {

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/not_xml3.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            assertThrows(NotXmlException.class, () -> searcher.getValuesByPath(new LinkedList<>()));
        }
    }

    @Test
    void testGetQNameValuesByPath() throws Exception {

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/1000000032016050600000020.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            String referenceQNamePath = joinStringStack(new LinkedList<>() {
                {
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").toString());
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Header").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "Addressing").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "Relations").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "RelatesTo").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "ReferenceQName").toString());
                }
            });

            String referenceMessageIDPath = joinStringStack(new LinkedList<>() {
                {
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").toString());
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Header").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "Addressing").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "Relations").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "RelatesTo").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "MessageID").toString());
                }
            });

            List<String> qnamePathList = new LinkedList<>() {
                {
                    add(referenceQNamePath);
                    add(referenceMessageIDPath);
                }
            };

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            Map<String, String> resultMap = searcher.getValuesByPath(qnamePathList);

            // работаем в обычном режиме и получаем просто строку
            assertEquals("ns3:ApprovalStateChange", resultMap.get(referenceQNamePath));
            // второе поле тоже нашлось
            assertEquals("1000770022016051000000411", resultMap.get(referenceMessageIDPath));

            searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            resultMap = searcher.getValuesByPath(qnamePathList, new HashSet<>() {
                {
                    add(new QName("http://xsd.gspvd/v001/addressing", "ReferenceQName").toString());
                }
            });

            // а теперь разрешили QName
            assertEquals(
                    "{http://schemas.xmlsoap.org/soap/envelope/}ApprovalStateChange",
                    resultMap.get(referenceQNamePath));

            // второе поле не изменилось
            assertEquals("1000770022016051000000411", resultMap.get(referenceMessageIDPath));
        }
    }

    @Test
    void testGetQNameValuesByPath2() throws Exception {
        String storageKeyPath = joinStringStack(new LinkedList<>() {
            {
                add(new QName("http://ru/pvdnp/smev3/cm", "OVOperation").toString());
                add(new QName("http://ru/pvdnp/smev3/cm", "Attachments").toString());
                add(new QName("http://ru/pvdnp/smev3/cm", "StorageKey").toString());
            }
        });

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/ov_cbcinit.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            List<String> resultList = searcher.getValuesByPath(storageKeyPath);
            Assertions.assertEquals(3, resultList.size());
            Assertions.assertTrue(resultList.contains("KEY1"));
            Assertions.assertTrue(resultList.contains("KEY2"));
            Assertions.assertTrue(resultList.contains("KEY3"));
        }
    }

    @Test
    void testGetQNameValuesByPath3() throws Exception {
        String storageKeyPath = joinStringStack(new LinkedList<>() {
            {
                add(new QName("http://ru/pvdnp/smev3/cm", "OVOperation").toString());
                add(new QName("http://ru/pvdnp/smev3/cm", "StorageKey").toString());
            }
        });

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/ov_cbcinit.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));

            List<String> qnamePathList = new LinkedList<>() {
                {
                    add(storageKeyPath);
                }
            };

            Map<String, String> result = searcher.getValuesByPath(qnamePathList);
            Assertions.assertEquals(1, result.size());
            Assertions.assertEquals("StorageKey", result.get(storageKeyPath));
        }
    }

    @Test
    void testGetQNameValuesByPath4() throws Exception {
        String attachmentStorageKeyPath = joinStackToString(new LinkedList<>() {
            {
                add(new QName("http://ru/pvdnp/smev3/cm", "OVOperation").toString());
                add(new QName("http://ru/pvdnp/smev3/cm", "Attachments").toString());
                add(new QName("http://ru/pvdnp/smev3/cm", "StorageKey").toString());
            }
        });

        String storageKeyPath = joinStackToString(new LinkedList<>() {
            {
                add(new QName("http://ru/pvdnp/smev3/cm", "OVOperation").toString());
                add(new QName("http://ru/pvdnp/smev3/cm", "StorageKey").toString());
            }
        });

        Set<String> qNamePathSet = new HashSet<>() {
            {
                add(attachmentStorageKeyPath);
                add(storageKeyPath);
            }
        };

        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/ov_cbcinit.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            Map<String, List<String>> resultMap = searcher.getValuesByQNamePathSet(qNamePathSet);
            Assertions.assertEquals(2, resultMap.size());

            Assertions.assertTrue(resultMap.containsKey(storageKeyPath));
            List<String> storageKeyList = resultMap.get(storageKeyPath);
            Assertions.assertEquals(1, storageKeyList.size());
            Assertions.assertEquals("StorageKey", storageKeyList.getFirst());

            Assertions.assertTrue(resultMap.containsKey(attachmentStorageKeyPath));
            List<String> resultList = resultMap.get(attachmentStorageKeyPath);

            Assertions.assertTrue(resultList.contains("KEY1"));
            Assertions.assertTrue(resultList.contains("KEY2"));
            Assertions.assertTrue(resultList.contains("KEY3"));
        }
    }

    @Test
    void testSplitPathIntoParentAndChild() {
        String path = "{AAA}B/{CCC}D";
        XPathSearcher.Pair<String, String> result = splitPathIntoParentAndChild(path);
        Assertions.assertEquals("/{AAA}B", result.getFirst());
        Assertions.assertEquals("{CCC}D", result.getSecond());

        path = "/{AAA}B/{CCC}D";
        result = splitPathIntoParentAndChild(path);
        Assertions.assertEquals("/{AAA}B", result.getFirst());
        Assertions.assertEquals("{CCC}D", result.getSecond());

        path = "/";
        result = splitPathIntoParentAndChild(path);
        Assertions.assertNull(result.getFirst());
        Assertions.assertNull(result.getSecond());

        path = "";
        result = splitPathIntoParentAndChild(path);
        Assertions.assertNull(result.getFirst());
        Assertions.assertNull(result.getSecond());

        path = "/{http://ru/pvdnp/smev3/cm}OVOperation";
        result = splitPathIntoParentAndChild(path);
        Assertions.assertEquals("/", result.getFirst());
        Assertions.assertEquals("{http://ru/pvdnp/smev3/cm}OVOperation", result.getSecond());

        path = "/{http://ru/pvdnp/smev3/cm}OVOperation/{http://ru/pvdnp/smev3/cm}StorageKey";
        result = splitPathIntoParentAndChild(path);
        Assertions.assertEquals("/{http://ru/pvdnp/smev3/cm}OVOperation", result.getFirst());
        Assertions.assertEquals("{http://ru/pvdnp/smev3/cm}StorageKey", result.getSecond());
    }

    @Test
    void testGetChildElementsQNameByPath1() throws Exception {
        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/ov_cbcinit.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            List<String> childList = searcher.getChildElementsQNameByPath("/");
            Assertions.assertEquals(1, childList.size());
            Assertions.assertEquals("{http://ru/pvdnp/smev3/cm}OVOperation", childList.getFirst());
        }
    }

    @Test
    void testGetChildElementsQNameByPath2() throws Exception {
        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/ov_cbcinit.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            List<String> childList = searcher.getChildElementsQNameByPath(
                    "/{http://ru/pvdnp/smev3/cm}OVOperation/{http://ru/pvdnp/smev3/cm}Attachments");
            Assertions.assertEquals(3, childList.size());
            Assertions.assertEquals("{http://ru/pvdnp/smev3/cm}StorageKey", childList.get(0));
            Assertions.assertEquals("{http://ru/pvdnp/smev3/cm}StorageKey", childList.get(1));
            Assertions.assertEquals("{http://ru/pvdnp/smev3/cm}StorageKey", childList.get(2));
        }
    }

    @Test
    void testGetQNameByPath() throws Exception {
        try (InputStream in = XPathSearcherTest.class.getResourceAsStream("/fxml/2000077012019042503315561.xml")) {
            byte[] xml = new byte[Objects.requireNonNull(in).available()];
            Assertions.assertEquals(xml.length, in.read(xml));

            XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(xml));
            List<QName> childList = searcher.getQNameByPath(joinStringStack(new LinkedList<>() {
                {
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").toString());
                    add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "Header").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "Addressing").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "Relations").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "RelatesTo").toString());
                    add(new QName("http://xsd.gspvd/v001/addressing", "ReferenceQName").toString());
                }
            }));
            Assertions.assertEquals(1, childList.size());
            Assertions.assertEquals(
                    QName.valueOf("{http://schemas.xmlsoap.org/soap/envelope/}Form"), childList.getFirst());
        }
    }
}
