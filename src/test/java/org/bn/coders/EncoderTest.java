/*
 Copyright 2006-2011 Abdulla Abdurakhmanov (abdulla@latestbit.com)
 Original sources are available at www.latestbit.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.bn.coders;

import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;

import org.bn.IEncoder;
import org.bn.coders.test_asn.Config;
import org.bn.coders.test_asn.ContentSchema;
import org.bn.coders.test_asn.Data;
import org.bn.coders.test_asn.DataSeq;
import org.bn.coders.test_asn.ITUSequence;
import org.bn.coders.test_asn.NullSequence;
import org.bn.coders.test_asn.SequenceWithEnum;
import org.bn.coders.test_asn.SequenceWithNull;
import org.bn.coders.test_asn.Set7;
import org.bn.coders.test_asn.StringArray;
import org.bn.coders.test_asn.TaggedNullSequence;
import org.bn.coders.test_asn.TestOCT;
import org.bn.coders.test_asn.TestOID;
import org.bn.coders.test_asn.TestPRN;
import org.bn.coders.test_asn.TestRecursiveDefinetion;
import org.bn.coders.test_asn.TestTaggedSetInSet;
import org.bn.coders.test_asn.UTF8StringArray;
import org.bn.utils.ByteTools;


public abstract class EncoderTest extends TestCase {
    private CoderTestUtilities coderTestUtils;
    
    public EncoderTest(String sTestName, CoderTestUtilities coderTestUtils) {
        super(sTestName);
        this.coderTestUtils = coderTestUtils;
    }        
    
    protected void printEncoded(String details,IEncoder encoder, Object obj) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        encoder.encode(obj, outputStream);
        System.out.println("Encoded by "+encoder.getClass()+" ("+details+") : " + ByteTools.byteArrayToHexString(outputStream.toByteArray()));        
    }
    
    protected abstract <T> IEncoder<T> newEncoder() throws Exception;
    
    /**
     * @see Encoder#encode(T,OutputStream)
     */
    public void testEncodeChoice() throws Exception {
        IEncoder<Data> encoder = newEncoder();
        assertNotNull(encoder);
        Data choice = new Data();        

        choice.selectBinary( new TestOCT( new byte[] { (byte)0xFF } ) );
        printEncoded("Choice boxed octet",encoder,choice);
        checkEncoded(encoder, choice, coderTestUtils.createDataChoiceTestOCTBytes());

        choice.selectSimpleType("aaaaaaa");
        printEncoded("Choice string", encoder,choice);
        checkEncoded(encoder, choice, coderTestUtils.createDataChoiceSimpleTypeBytes());
        
        choice.selectBooleanType(true);
        printEncoded("Choice boolean",encoder,choice);
        checkEncoded(encoder, choice, coderTestUtils.createDataChoiceBooleanBytes());
        
        choice.selectIntBndType(7);
        printEncoded("Choice boxed int",encoder,choice);
        checkEncoded(encoder, choice, coderTestUtils.createDataChoiceIntBndBytes());
        
        choice.selectPlain(new TestPRN("bbbbbb"));
        printEncoded("Choice plain",encoder,choice);
        checkEncoded(encoder, choice, coderTestUtils.createDataChoicePlainBytes());
        
        choice.selectSimpleOctType(new byte[10] );
        printEncoded("Choice simple octet",encoder,choice);
        choice.selectIntType(7L);
        printEncoded("Choice simple int",encoder,choice);
    }
        
    /**
     * @see Encoder#encode(T,OutputStream)
     */
    public void testEncode() throws Exception {
        IEncoder<DataSeq> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("SequenceMO test",encoder, coderTestUtils.createDataSeqMO());
        checkEncoded(encoder, coderTestUtils.createDataSeqMO(), coderTestUtils.createDataSeqMOBytes());
    
        printEncoded("Sequence test",encoder, coderTestUtils.createDataSeq());
        checkEncoded(encoder, coderTestUtils.createDataSeq(), coderTestUtils.createDataSeqBytes());
        
    }

    /**
     * @see Encoder#encode(T,OutputStream)
     */
    public void testITUEncode() throws Exception {
        IEncoder<ITUSequence> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("ITUSequence test",encoder, coderTestUtils.createITUSeq());
        checkEncoded(encoder, coderTestUtils.createITUSeq(), coderTestUtils.createITUSeqBytes());
    }

    protected void checkEncoded(IEncoder encoder, Object obj, byte[] standard) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        encoder.encode(obj, outputStream);
        byte[] array = outputStream.toByteArray();
        assertEquals( array.length , standard. length);
        for(int i=0;i<array.length;i++) {            
            assertEquals(array[i],standard[i]);
        }
    }
    
    public void testNullEncode() throws Exception {
        IEncoder<NullSequence> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("NullSequence test",encoder, coderTestUtils.createNullSeq());
        checkEncoded(encoder, coderTestUtils.createNullSeq(), coderTestUtils.createNullSeqBytes());
    }
    
    public void testTaggedNullEncode() throws Exception {
        IEncoder<TaggedNullSequence> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("TaggedNullSequence test",encoder, coderTestUtils.createTaggedNullSeq());
        checkEncoded(encoder, coderTestUtils.createTaggedNullSeq(), coderTestUtils.createTaggedNullSeqBytes());        
    }
   
    public void testEnum() throws Exception {
        IEncoder<ContentSchema> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("Enum test",encoder, coderTestUtils.createEnum());
        checkEncoded(encoder, coderTestUtils.createEnum(), coderTestUtils.createEnumBytes());
    }    

    public void testSequenceWithEnum() throws Exception {
        IEncoder<SequenceWithEnum> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("Sequence Enum test",encoder, coderTestUtils.createSequenceWithEnum());
        checkEncoded(encoder, coderTestUtils.createSequenceWithEnum(), coderTestUtils.createSequenceWithEnumBytes());
    }
    
    public void testSequenceOfString() throws Exception {
        IEncoder<StringArray> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("Sequence Of String",encoder, coderTestUtils.createStringArray());
        checkEncoded(encoder, coderTestUtils.createStringArray(), coderTestUtils.createStringArrayBytes());
    }
    
    
    public void testRecursiveDefinition() throws Exception {
        IEncoder<TestRecursiveDefinetion> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("Recursive test",encoder, coderTestUtils.createTestRecursiveDefinition());
        checkEncoded(encoder, coderTestUtils.createTestRecursiveDefinition(), coderTestUtils.createTestRecursiveDefinitionBytes());
    }
    
    
    public void testEncodeInteger() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("Unbounded integer test",encoder, coderTestUtils.createUnboundedTestInteger());
        checkEncoded(encoder, coderTestUtils.createUnboundedTestInteger(), coderTestUtils.createUnboundedTestIntegerBytes());
        printEncoded("Integer2_12 test",encoder, coderTestUtils.createTestInteger2_12());
        checkEncoded(encoder, coderTestUtils.createTestInteger2_12(), coderTestUtils.createTestInteger2_12Bytes());
        
        printEncoded("IntegerR test",encoder, coderTestUtils.createTestIntegerR());
        checkEncoded(encoder, coderTestUtils.createTestIntegerR(), coderTestUtils.createTestIntegerRBytes());        
        
        printEncoded("Integer4 test",encoder, coderTestUtils.createTestInteger4());
        checkEncoded(encoder, coderTestUtils.createTestInteger4(), coderTestUtils.createTestInteger4Bytes());
        printEncoded("Integer3 test",encoder, coderTestUtils.createTestInteger3());
        checkEncoded(encoder, coderTestUtils.createTestInteger3(), coderTestUtils.createTestInteger3Bytes());
        printEncoded("Integer2 test",encoder, coderTestUtils.createTestInteger2());
        checkEncoded(encoder, coderTestUtils.createTestInteger2(), coderTestUtils.createTestInteger2Bytes());

        
        printEncoded("Integer1 test",encoder, coderTestUtils.createTestInteger1());
        checkEncoded(encoder, coderTestUtils.createTestInteger1(), coderTestUtils.createTestInteger1Bytes());        
                
    }
    
    public void testEncodeString() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("TestPRN",encoder, coderTestUtils.createTestPRN());
        checkEncoded(encoder, coderTestUtils.createTestPRN(), coderTestUtils.createTestPRNBytes());
        
        printEncoded("TestOCT",encoder, coderTestUtils.createTestOCT());
        checkEncoded(encoder, coderTestUtils.createTestOCT(), coderTestUtils.createTestOCTBytes());        
    }
    
    public void testEncodeNegativeInteger() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("Negative integer test",encoder, coderTestUtils.createTestNI());
        checkEncoded(encoder, coderTestUtils.createTestNI(), coderTestUtils.createTestNIBytes());
        printEncoded("Negative integer test 2",encoder, coderTestUtils.createTestNI2());
        checkEncoded(encoder, coderTestUtils.createTestNI2(), coderTestUtils.createTestNI2Bytes());        
    }
    
    public void testEncodeSet() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("Set test",encoder, coderTestUtils.createSet());            
        checkEncoded(encoder, coderTestUtils.createSet(), coderTestUtils.createSetBytes());
    }
    
    public void testEncodeBitString() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("TestBitStr test",encoder, coderTestUtils.createTestBitStr());            
        checkEncoded(encoder, coderTestUtils.createTestBitStr(), coderTestUtils.createTestBitStrBytes());
    }

    public void testEncodeBitStringSmall() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("TestBitStrSmall test",encoder, coderTestUtils.createTestBitStrSmall());            
        checkEncoded(encoder, coderTestUtils.createTestBitStrSmall(), coderTestUtils.createTestBitStrSmallBytes());
    }

    public void testEncodeBitStringBnd() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("TestBitStrBnd test",encoder, coderTestUtils.createTestBitStrBnd());            
        checkEncoded(encoder, coderTestUtils.createTestBitStrBnd(), coderTestUtils.createTestBitStrBndBytes());
    }
    
    public void testEncodeUnicodeString() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("TestUnicode test",encoder, coderTestUtils.createUnicodeStr());            
        checkEncoded(encoder, coderTestUtils.createUnicodeStr(), coderTestUtils.createUnicodeStrBytes());
    }
    
    public void testEncodeVersion1_2() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("TestEncodeVersion_1_2: ",encoder, coderTestUtils.createTestSequenceV12());            
        checkEncoded(encoder, coderTestUtils.createTestSequenceV12(), coderTestUtils.createTestSequenceV12Bytes());        
    }
    
    public void testEncodeChoiceInChoice() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("EncodeChoiceInChoice: ",encoder, coderTestUtils.createChoiceInChoice());            
        checkEncoded(encoder, coderTestUtils.createChoiceInChoice(), coderTestUtils.createChoiceInChoiceBytes());        
    }

    public void testEncodeChoiceInChoice2() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("EncodeChoiceInChoice2: ",encoder, coderTestUtils.createChoiceInChoice2());            
        checkEncoded(encoder, coderTestUtils.createChoiceInChoice2(), coderTestUtils.createChoiceInChoice2Bytes());        
    }

    public void testEncodeChoiceInChoice3() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("EncodeChoiceInChoice3: ",encoder, coderTestUtils.createChoiceInChoice3());            
        checkEncoded(encoder, coderTestUtils.createChoiceInChoice3(), coderTestUtils.createChoiceInChoice3Bytes());        
    }

    public void testEncodeTaggedSeqInSeq() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("EncodeTaggedSeqInSeq: ",encoder, coderTestUtils.createTaggedSeqInSeq());            
        checkEncoded(encoder, coderTestUtils.createTaggedSeqInSeq(), coderTestUtils.createTaggedSeqInSeqBytes());        
    }

    public void testEncodeReals() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("EncodeTestReal1.5: ",encoder, coderTestUtils.createTestReal1_5());            
        checkEncoded(encoder, coderTestUtils.createTestReal1_5(), coderTestUtils.createTestReal1_5Bytes());        
        printEncoded("EncodeTestReal0.5: ",encoder, coderTestUtils.createTestReal0_5());            
        checkEncoded(encoder, coderTestUtils.createTestReal0_5(), coderTestUtils.createTestReal0_5Bytes());                
        printEncoded("EncodeTestReal2: ",encoder, coderTestUtils.createTestReal2());            
        checkEncoded(encoder, coderTestUtils.createTestReal2(), coderTestUtils.createTestReal2Bytes());        
        printEncoded("EncodeTestRealBig: ",encoder, coderTestUtils.createTestRealBig());            
        checkEncoded(encoder, coderTestUtils.createTestRealBig(), coderTestUtils.createTestRealBigBytes());        
        
    }
    
    public void testSequenceWithNull() throws Exception {
        IEncoder<SequenceWithNull> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("SequenceWithNull test",encoder, coderTestUtils.createSeqWithNull());
        checkEncoded(encoder, coderTestUtils.createSeqWithNull(), coderTestUtils.createSeqWithNullBytes());
    }
    
    public void testEncodeLongTag() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("EncodeTestLongTag: ",encoder, coderTestUtils.createTestLongTag());
        checkEncoded(encoder, coderTestUtils.createTestLongTag(), coderTestUtils.createTestLongTagBytes());                
    }    
    
    public void testEncodeLongTag2() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("EncodeTestLongTag2: ",encoder, coderTestUtils.createTestLongTag2());
        checkEncoded(encoder, coderTestUtils.createTestLongTag2(), coderTestUtils.createTestLongTag2Bytes());                
    }

    public void testSequenceOfUTFString() throws Exception {
        IEncoder<UTF8StringArray> encoder = newEncoder();
        assertNotNull(encoder);
        printEncoded("Sequence Of UTF8String",encoder, coderTestUtils.createUTF8StringArray());
        checkEncoded(encoder, coderTestUtils.createUTF8StringArray(), coderTestUtils.createUTF8StringArrayBytes());
    }
    
    public void testEncodeOID() throws Exception {
        IEncoder encoder = newEncoder();
        assertNotNull(encoder);
        //
        TestOID testOID = coderTestUtils.createTestOID1();
        printEncoded("OID "+testOID.getValue().getValue(), encoder, testOID);
        checkEncoded(encoder, coderTestUtils.createTestOID1(), coderTestUtils.createTestOID1Bytes());
        //
        testOID = coderTestUtils.createTestOID2();
        printEncoded("OID " + testOID.getValue().getValue(), encoder, testOID);
        checkEncoded(encoder, coderTestUtils.createTestOID2(), coderTestUtils.createTestOID2Bytes());
        //
        testOID = coderTestUtils.createTestOID3();
        printEncoded("OID " + testOID.getValue().getValue(), encoder, testOID);
        checkEncoded(encoder, coderTestUtils.createTestOID3(), coderTestUtils.createTestOID3Bytes());
        //
        testOID = coderTestUtils.createTestOID4();
        printEncoded("OID " + testOID.getValue().getValue(), encoder, testOID);
        checkEncoded(encoder, coderTestUtils.createTestOID4(), coderTestUtils.createTestOID4Bytes());
    }
    
    public void testEncodeTaggedSet() throws Exception {
    	IEncoder<?> encoder = newEncoder();
        assertNotNull(encoder);
        //
        Config taggedSet = coderTestUtils.createTaggedSet();
        printEncoded("TaggedSet",encoder, taggedSet);
        checkEncoded(encoder, coderTestUtils.createTaggedSet(), coderTestUtils.createTaggedSetBytes());
        
    }

    public void testEncodeTaggedSetInSet() throws Exception {
    	IEncoder<?> encoder = newEncoder();
        assertNotNull(encoder);
        //
        TestTaggedSetInSet taggedSet = coderTestUtils.createTaggedSetInSet();
        printEncoded("TaggedSetInSet",encoder, taggedSet);
        checkEncoded(encoder, coderTestUtils.createTaggedSetInSet(), coderTestUtils.createTaggedSetInSetBytes());
        
        Set7 set7 = coderTestUtils.createSet7();
        printEncoded("Set7",encoder, set7);
        checkEncoded(encoder, coderTestUtils.createSet7(), coderTestUtils.createSet7Bytes());
    }
    
}
