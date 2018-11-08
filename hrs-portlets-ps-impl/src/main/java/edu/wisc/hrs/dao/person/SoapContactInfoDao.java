/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.wisc.hrs.dao.person;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.DecoratedCacheType;

import edu.wisc.hr.dao.person.ContactInfoDao;
import edu.wisc.hr.dm.person.Address;
import edu.wisc.hr.dm.person.HomeAddress;
import edu.wisc.hr.dm.person.Job;
import edu.wisc.hr.dm.person.OfficeAddress;
import edu.wisc.hr.dm.person.PersonInformation;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.person.req.EmplidTypeShape;
import edu.wisc.hrs.xdm.person.req.GetCompIntfcUWPORTAL1PERSON;
import edu.wisc.hrs.xdm.person.res.GetCompIntfcUWPORTAL1PERSONResponse;
import edu.wisc.hrs.xdm.person.res.UwAddress4VwTypeShape;
import edu.wisc.hrs.xdm.person.res.UwHrPhoneVwTypeShape;
import edu.wisc.hrs.xdm.person.res.UwHrSecDptVwTypeShape;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 * 
 * Requires a Spring {@link WebServiceOperations} be set.
 * 
 */
@Repository("soapContactInfoDao")
public class SoapContactInfoDao extends BaseHrsSoapDao implements ContactInfoDao {
    private WebServiceOperations webServiceOperations;
    
    @Autowired
    public void setWebServiceOperations(@Qualifier("personWebServiceTemplate") WebServiceOperations webServiceOperations) {
        this.webServiceOperations = webServiceOperations;
    }
    
    @Override
    protected WebServiceOperations getWebServiceOperations() {
        return this.webServiceOperations;
    }
    
	/* (non-Javadoc)
     * @see edu.wisc.ws.client.support.PeraonalDataDao#getPersonalData(java.lang.String)
     */
	@Override
	@Cacheable(cacheName="contactInformation", decoratedCacheType=DecoratedCacheType.SELF_POPULATING_CACHE, selfPopulatingTimeout=20000, exceptionCacheName="hrsUnknownExceptionCache")
    public PersonInformation getPersonalData(String emplId) {
	    final GetCompIntfcUWPORTAL1PERSON request = this.createRequest(emplId);
	    
	    GetCompIntfcUWPORTAL1PERSONResponse response = this.internalInvoke(request);
	    
	    return this.mapPerson(response);
	}

    protected GetCompIntfcUWPORTAL1PERSON createRequest(String emplId) {
        final EmplidTypeShape value = HrsUtils.createValue(EmplidTypeShape.class, emplId);
	    
        GetCompIntfcUWPORTAL1PERSON request = new GetCompIntfcUWPORTAL1PERSON();
	    request.setEmplid(value);
        return request;
    }
    
    protected PersonInformation mapPerson(GetCompIntfcUWPORTAL1PERSONResponse psPersonalData) {
        if (psPersonalData == null) {
            return null;
        }
        
        final PersonInformation personalData = new PersonInformation();
        
        //Copy name
        personalData.setName((String)HrsUtils.getValue(psPersonalData.getName()));
        
        //Copy email
        personalData.setEmail((String)HrsUtils.getValue(psPersonalData.getEmailAddr()));
        
        //Copy Visa/Campus info
        personalData.setOnVisa("Y".equals(HrsUtils.getValue(psPersonalData.getVisaPermitClass())));
        personalData.setMadisonEmpl("Y".equals(HrsUtils.getValue(psPersonalData.getDeptLocBtn())));

        //Setup Jobs list
        final List<UwHrSecDptVwTypeShape> additionalJobs = psPersonalData.getUwHrSecDptVws();
        final List<Job> jobs = personalData.getJobs(); 
            
        //load primary department
        final Job primaryJob = new Job();
        primaryJob.setDepartmentName((String)HrsUtils.getValue(psPersonalData.getDeptDescr()));
        primaryJob.setTitle((String)HrsUtils.getValue(psPersonalData.getUwWorkingTitle()));
        
        //load other departments
        getJobs(additionalJobs, jobs, primaryJob);
        
        personalData.setPrimaryJob(primaryJob);
        
        //Populate address data
        this.populateAddresses(psPersonalData, personalData);
        
        //Populate phone data
        populatePhones(psPersonalData, personalData);
        
        return personalData;
    }

    protected void populatePhones(GetCompIntfcUWPORTAL1PERSONResponse psPersonalData,
            final PersonInformation personalData) {
        final List<UwHrPhoneVwTypeShape> uwHrPhoneVws = psPersonalData.getUwHrPhoneVws();
        for (final UwHrPhoneVwTypeShape uwHrPhoneVwTypeShape : uwHrPhoneVws) {
            final String phoneType = HrsUtils.getValue(uwHrPhoneVwTypeShape.getPhoneType());
            if ("HOME".equals(phoneType)) {
                HomeAddress homeAddress = personalData.getHomeAddress();
                if (homeAddress == null) {
                    homeAddress = new HomeAddress();
                    personalData.setHomeAddress(homeAddress);
                }
                final String phone = HrsUtils.getValue(uwHrPhoneVwTypeShape.getPhone());
                homeAddress.setPrimaryPhone(phone);
            }
            else if ("BUSN".equals(phoneType)) {
                OfficeAddress officeAddress = personalData.getOfficeAddress();
                if (officeAddress == null) {
                    officeAddress = new OfficeAddress();
                    personalData.setOfficeAddress(officeAddress);
                }
                final String phone = HrsUtils.getValue(uwHrPhoneVwTypeShape.getPhone());
                officeAddress.setPrimaryPhone(phone);
            }
            else if ("BSNO".equals(phoneType)) {
                OfficeAddress officeAddress = personalData.getOfficeAddress();
                if (officeAddress == null) {
                    officeAddress = new OfficeAddress();
                    personalData.setOfficeAddress(officeAddress);
                }
                final String phone = HrsUtils.getValue(uwHrPhoneVwTypeShape.getPhone());
              officeAddress.setOtherPhone(phone);
            }
            else {
                logger.warn("Encountered unsupported phone type: '" + phoneType + "'\n" + psPersonalData);
            }
        }
    }

    protected void populateAddresses(GetCompIntfcUWPORTAL1PERSONResponse psPersonalData,
            final PersonInformation personalData) {
        final List<UwAddress4VwTypeShape> uwAddress4Vws = psPersonalData.getUwAddress4Vws();
        for (final UwAddress4VwTypeShape uwAddress4VwTypeShape : uwAddress4Vws) {
            final String addressType = HrsUtils.getValue(uwAddress4VwTypeShape.getAddressType());
            if ("HOME".equals(addressType)) {
                final HomeAddress homeAddress = new HomeAddress();
                
                final String releaseHomeInfo = HrsUtils.getValue(psPersonalData.getUwRelHomeInfSw());
                homeAddress.setReleaseHomeAddress("Y".equals(releaseHomeInfo));
                
                this.populateAddress(homeAddress, uwAddress4VwTypeShape);
                
                personalData.setHomeAddress(homeAddress);
            }
            else if ("BUSN".equals(addressType)) {
                final OfficeAddress officeAddress = new OfficeAddress();
                
                this.populateAddress(officeAddress, uwAddress4VwTypeShape);
                
                personalData.setOfficeAddress(officeAddress);
            }
            else {
                logger.warn("Encountered unsupported address type: '" + addressType + "'\n" + psPersonalData);
            }
        }
    }

    protected void getJobs(final List<UwHrSecDptVwTypeShape> uwHrSecDptVws, List<Job> jobs, Job primaryJob) {
        for (final UwHrSecDptVwTypeShape uwHrSecDptVwTypeShape : uwHrSecDptVws) {
            final Job job = new Job();
            
            job.setId((Integer)HrsUtils.getValue(uwHrSecDptVwTypeShape.getEmplRcd()));
            job.setTitle((String)HrsUtils.getValue(uwHrSecDptVwTypeShape.getUwWorkingTitle1()));
            job.setDepartmentName((String)HrsUtils.getValue(uwHrSecDptVwTypeShape.getDescr()));
            
            //Set the jobId of the primary job
            if (StringUtils.equals(primaryJob.getTitle(), job.getTitle()) && 
                    StringUtils.equals(primaryJob.getDepartmentName(), job.getDepartmentName())) {
                primaryJob.setId(job.getId());
            }
            
            jobs.add(job);
        }
    }
    
    protected void populateAddress(Address address, UwAddress4VwTypeShape psAddress) {
        address.setRoomNumber((String)HrsUtils.getValue(psAddress.getUwRoomNbr()));
        address.setMailDrop((String)HrsUtils.getValue(psAddress.getUwMailDropId()));
        address.setLocation((String)HrsUtils.getValue(psAddress.getLocation()));
        address.setAddress1((String)HrsUtils.getValue(psAddress.getAddress1()));
        address.setAddress2((String)HrsUtils.getValue(psAddress.getAddress2()));
        address.setAddress3((String)HrsUtils.getValue(psAddress.getAddress3()));
        address.setCity((String)HrsUtils.getValue(psAddress.getCity()));
        address.setState((String)HrsUtils.getValue(psAddress.getState()));
        address.setZip((String)HrsUtils.getValue(psAddress.getPostal()));
    }
}
