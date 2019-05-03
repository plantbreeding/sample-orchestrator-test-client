package org.brapi.test.SampleOrchestratorServer.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.brapi.test.SampleOrchestratorServer.model.json.ServicePlatformMarkerTypeEnum;
import org.brapi.test.SampleOrchestratorServer.repository.ObjectConverter;

@Entity
@Table(name = "vendor_specification_service")
public class VendorSpecificationServiceEntity extends BaseEntity {
	@Column(length=2000)
	private String serviceDescription = null;
	@Column
	private String serviceId = null;
	@Column
	private String serviceName = null;
	@Column
	private ServicePlatformMarkerTypeEnum servicePlatformMarkerType = null;
	@Column
	private String servicePlatformName = null;
	@Column
	@Convert(converter = ObjectConverter.class)
	private Object specificRequirements = null;
	@ManyToOne
	private VendorSpecificationEntity vendorSpecification = null;

	public VendorSpecificationEntity getVendorSpecification() {
		return vendorSpecification;
	}

	public void setVendorSpecification(VendorSpecificationEntity vendorSpecification) {
		this.vendorSpecification = vendorSpecification;
	}

	public VendorSpecificationServiceEntity serviceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
		return this;
	}
	
	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public VendorSpecificationServiceEntity serviceId(String serviceId) {
		this.serviceId = serviceId;
		return this;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public VendorSpecificationServiceEntity serviceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public VendorSpecificationServiceEntity servicePlatformMarkerType(
			ServicePlatformMarkerTypeEnum servicePlatformMarkerType) {
		this.servicePlatformMarkerType = servicePlatformMarkerType;
		return this;
	}
	
	public ServicePlatformMarkerTypeEnum getServicePlatformMarkerType() {
		return servicePlatformMarkerType;
	}

	public void setServicePlatformMarkerType(ServicePlatformMarkerTypeEnum servicePlatformMarkerType) {
		this.servicePlatformMarkerType = servicePlatformMarkerType;
	}

	public VendorSpecificationServiceEntity servicePlatformName(String servicePlatformName) {
		this.servicePlatformName = servicePlatformName;
		return this;
	}

	public String getServicePlatformName() {
		return servicePlatformName;
	}

	public void setServicePlatformName(String servicePlatformName) {
		this.servicePlatformName = servicePlatformName;
	}

	public VendorSpecificationServiceEntity specificRequirements(Object specificRequirements) {
		this.specificRequirements = specificRequirements;
		return this;
	}
	
	public Object getSpecificRequirements() {
		return specificRequirements;
	}

	public void setSpecificRequirements(Object specificRequirements) {
		this.specificRequirements = specificRequirements;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		VendorSpecificationServiceEntity vendorSpecificationServiceEntity = (VendorSpecificationServiceEntity) o;
		return Objects.equals(this.serviceDescription, vendorSpecificationServiceEntity.serviceDescription)
				&& Objects.equals(this.serviceId, vendorSpecificationServiceEntity.serviceId)
				&& Objects.equals(this.serviceName, vendorSpecificationServiceEntity.serviceName)
				&& Objects.equals(this.servicePlatformMarkerType, vendorSpecificationServiceEntity.servicePlatformMarkerType)
				&& Objects.equals(this.servicePlatformName, vendorSpecificationServiceEntity.servicePlatformName)
				&& Objects.equals(this.specificRequirements, vendorSpecificationServiceEntity.specificRequirements);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceDescription, serviceId, serviceName, servicePlatformMarkerType, servicePlatformName,
				specificRequirements);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class VendorSpecificationService {\n");

		sb.append("    serviceDescription: ").append(toIndentedString(serviceDescription)).append("\n");
		sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
		sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
		sb.append("    servicePlatformMarkerType: ").append(toIndentedString(servicePlatformMarkerType)).append("\n");
		sb.append("    servicePlatformName: ").append(toIndentedString(servicePlatformName)).append("\n");
		sb.append("    specificRequirements: ").append(toIndentedString(specificRequirements)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
