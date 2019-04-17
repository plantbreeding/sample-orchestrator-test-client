package org.brapi.test.SampleOrchestratorServer.model.entity;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.brapi.test.SampleOrchestratorServer.repository.HashMapConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vendor_specification")
public class VendorSpecificationEntity extends BaseEntity {

	@Column(length = 2048)
	@Convert(converter= HashMapConverter.class)
	private Map<String, Object> additionalInfo = null;
	@OneToMany(mappedBy = "vendorSpecification")
	private List<VendorSpecificationServiceEntity> services = null;
	@OneToOne
	private VendorContactEntity vendorContactEntity = null;

	public VendorSpecificationEntity additionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = additionalInfo;
		return this;
	}

	public VendorSpecificationEntity putAdditionalInfoItem(String key, Object additionalInfoItem) {
		if (this.additionalInfo == null) {
			this.additionalInfo = null;
		}
		this.additionalInfo.put(key, additionalInfoItem);
		return this;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public VendorSpecificationEntity services(List<VendorSpecificationServiceEntity> services) {
		this.services = services;
		return this;
	}

	public VendorSpecificationEntity addServicesItem(VendorSpecificationServiceEntity servicesItem) {
		if (this.services == null) {
			this.services = new ArrayList<VendorSpecificationServiceEntity>();
		}
		this.services.add(servicesItem);
		return this;
	}

	public List<VendorSpecificationServiceEntity> getServices() {
		return services;
	}

	public void setServices(List<VendorSpecificationServiceEntity> services) {
		this.services = services;
	}

	public VendorSpecificationEntity vendorContactEntity(VendorContactEntity vendorContactEntity) {
		this.vendorContactEntity = vendorContactEntity;
		return this;
	}

	public VendorContactEntity getVendorContact() {
		return vendorContactEntity;
	}

	public void setVendorContact(VendorContactEntity vendorContactEntity) {
		this.vendorContactEntity = vendorContactEntity;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		VendorSpecificationEntity vendorSpecificationEntity = (VendorSpecificationEntity) o;
		return Objects.equals(this.additionalInfo, vendorSpecificationEntity.additionalInfo)
				&& Objects.equals(this.services, vendorSpecificationEntity.services)
				&& Objects.equals(this.vendorContactEntity, vendorSpecificationEntity.vendorContactEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(additionalInfo, services, vendorContactEntity);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class VendorSpecification {\n");

		sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
		sb.append("    services: ").append(toIndentedString(services)).append("\n");
		sb.append("    vendorContact: ").append(toIndentedString(vendorContactEntity)).append("\n");
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
