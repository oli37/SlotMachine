package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CostFunction  {

    private String name;
    private String owner; //which Airline
    private List<Proposal> proposalList = new ArrayList<>();

    private List<Integer> delayList = new LinkedList<>();
    private List<Float> priceList = new LinkedList<>();
    private List<Boolean> askList = new LinkedList<>();
    private List<String> labelList = new LinkedList<>();


    public CostFunction() {
    }

    public CostFunction(String name) {
        this.name = name;
    }

    public CostFunction(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public CostFunction(String name, String owner, List<Proposal> proposalList) {
        this.name = name;
        this.owner = owner;
        setProposalList(proposalList);
    }

    public CostFunction(String name, String owner, int from, int to, int incr, float stdPrice) {
        this.name = name;
        this.owner = owner;

        for (int delay = from; delay <= to; delay += incr) {
            priceList.add(stdPrice);
            delayList.add(delay);
            askList.add(true);
            labelList.add((delay <= 0 ? "" : "+") + delay + "min");
            proposalList.add(new Proposal(stdPrice, delay, true));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Proposal> getProposalList() {
        return proposalList;
    }

    public void addProposal(Proposal proposal) {
        proposalList.add(proposal);
        proposalToList(proposal);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Integer> getDelayList() {
        return delayList;
    }

    public List<Float> getPriceList() {
        return priceList;
    }

    public List<Boolean> getAskList() {
        return askList;
    }

    public List<String> getLabelList() {
        return labelList;
    }


    public void setAsk(int element, boolean value){
        assert element < askList.size();
        proposalList.get(element).setAsk(value);
        askList.set(element, value);
    }

    public void setPrice(int element, float value) {
        assert element < proposalList.size();
        proposalList.get(element).setPrice(value);
        priceList.set(element, value);
    }

    public void setProposalList(List<Proposal> proposalList) {
        this.proposalList = proposalList;
        proposalList.forEach(this::proposalToList);
    }

    private void proposalToList(Proposal prop ){
        priceList.add(prop.getPrice());
        delayList.add(prop.getDelay());
        askList.add(prop.isAsk());
        labelList.add((prop.getDelay() <= 0 ? "" : "+") + prop.getDelay() + "min");
    }

    @Override
    public String toString() {
        return "CostFunction{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", proposalList=" + proposalList +
                ", delayList=" + delayList +
                ", priceList=" + priceList +
                ", bidList=" + askList +
                ", labelList=" + labelList +
                '}';
    }
}


