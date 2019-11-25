package cn.xzy.core.entity;

import cn.xzy.core.pojo.specification.Specification;
import cn.xzy.core.pojo.specification.SpecificationOption;

import java.io.Serializable;
import java.util.List;

public class SpecResult  implements Serializable {
    private Specification specification;
    private List<SpecificationOption> specificationOptionList;

    public SpecResult() {
    }

    public SpecResult(Specification specification, List<SpecificationOption> specificationOptionList) {
        this.specification = specification;
        this.specificationOptionList = specificationOptionList;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<SpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
