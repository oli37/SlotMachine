package application;

import org.apache.commons.collections.list.PredicatedList;

import java.util.ArrayList;
import java.util.List;

public class CostFunction {

    private String name;
    private String owner; //which Airline
    private List<Proposal> proposalList = new ArrayList<>();

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
        this.proposalList = proposalList;
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

    public void setProposalList(List<Proposal> proposalList) {
        this.proposalList = proposalList;
    }

    public void addProposal(Proposal proposal) {
        proposalList.add(proposal);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "CostFunction{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", proposalList=" + proposalList +
                '}';
    }
}


