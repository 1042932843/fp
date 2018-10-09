package com.dyhl.dusky.huangchuanfp.Module.entity;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/5/15
 * @DESCRIPTION:
 */
public class PovertyCondition {
    String village;//村总数

    public String getOutpovertyfamily() {
        return outpovertyfamily;
    }

    public void setOutpovertyfamily(String outpovertyfamily) {
        this.outpovertyfamily = outpovertyfamily;
    }

    String outpovertyfamily;//脱贫户数

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getPovertyvillage() {
        return povertyvillage;
    }

    public void setPovertyvillage(String povertyvillage) {
        this.povertyvillage = povertyvillage;
    }

    public String getNotpovertyvillage() {
        return notpovertyvillage;
    }

    public void setNotpovertyvillage(String notpovertyvillage) {
        this.notpovertyvillage = notpovertyvillage;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getInpoverty() {
        return inpoverty;
    }

    public void setInpoverty(String inpoverty) {
        this.inpoverty = inpoverty;
    }

    public String getOutofpoverty() {
        return outofpoverty;
    }

    public void setOutofpoverty(String outofpoverty) {
        this.outofpoverty = outofpoverty;
    }

    public String getTotalfamily() {
        return totalfamily;
    }

    public void setTotalfamily(String totalfamily) {
        this.totalfamily = totalfamily;
    }

    public String getPovertyfamily() {
        return povertyfamily;
    }

    public void setPovertyfamily(String povertyfamily) {
        this.povertyfamily = povertyfamily;
    }

    public String getTotalpopulation() {
        return totalpopulation;
    }

    public void setTotalpopulation(String totalpopulation) {
        this.totalpopulation = totalpopulation;
    }

    public String getPovertypopulation() {
        return povertypopulation;
    }

    public void setPovertypopulation(String povertypopulation) {
        this.povertypopulation = povertypopulation;
    }

    public String getZcdy() {
        return zcdy;
    }

    public void setZcdy(String zcdy) {
        this.zcdy = zcdy;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOutvillage() {
        return outvillage;
    }

    public void setOutvillage(String outvillage) {
        this.outvillage = outvillage;
    }

    public String getInvillage() {
        return invillage;
    }

    public void setInvillage(String invillage) {
        this.invillage = invillage;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getOutwork() {
        return outwork;
    }

    public void setOutwork(String outwork) {
        this.outwork = outwork;
    }

    String povertyvillage;//贫困村总数
    String notpovertyvillage;//非贫困村总数
    String population;//脱贫人数
    String inpoverty;//未脱贫村数
    String outofpoverty;//已脱贫村数
    String totalfamily;//总户数
    String povertyfamily;//贫困户数
    String totalpopulation;//人口总数
    String povertypopulation;//贫困人口数
    String zcdy;//驻村队员数量
    String department;//帮扶单位数量
    String outvillage;//非贫困村贫困人口
    String invillage;//贫困村贫困人口
    String responsible;//帮扶责任人数量
    String outwork;//外出务工人数

    public String getBfzfl() {
        return bfzfl;
    }

    public void setBfzfl(String bfzfl) {
        this.bfzfl = bfzfl;
    }

    String bfzfl;//走访量
}
