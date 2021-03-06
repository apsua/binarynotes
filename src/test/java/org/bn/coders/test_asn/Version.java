
package org.bn.coders.test_asn;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1BoxedType;
import org.bn.annotations.ASN1Element;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.annotations.ASN1Sequence;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;
import org.bn.coders.TagClass;




    @ASN1PreparedElement
    @ASN1BoxedType ( name = "Version" )
    public class Version implements IASN1PreparedElement {
                
        

       @ASN1PreparedElement
       @ASN1Sequence ( name = "Version" , isSet = true )
       public static class VersionSequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "minor", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private Minor minor = null;
                
  
        @ASN1Element ( name = "major", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private Major major = null;
                
  
        
        public Minor getMinor () {
            return this.minor;
        }

        
        public boolean isMinorPresent () {
            return this.minor != null;
        }
        

        public void setMinor (Minor value) {
            this.minor = value;
        }
        
  
        
        public Major getMajor () {
            return this.major;
        }

        
        public boolean isMajorPresent () {
            return this.major != null;
        }
        

        public void setMajor (Major value) {
            this.major = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_VersionSequenceType;
        }

       private static IASN1PreparedElementData preparedData_VersionSequenceType = CoderFactory.getInstance().newPreparedElementData(VersionSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "Version", isOptional =  false , hasTag =  true, tag = 74, 
        tagClass =  TagClass.Application  , hasDefaultValue =  false  )
    
        private VersionSequenceType  value;        

        
        
        public Version () {
        }
        
        
        
        public void setValue(VersionSequenceType value) {
            this.value = value;
        }
        
        
        
        public VersionSequenceType getValue() {
            return this.value;
        }            
        

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(Version.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            