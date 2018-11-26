package br.edu.ulbra.election.election.output.v1;

/*TO TEST*/
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Voter Output information")
public class VoteOutput {

    @ApiModelProperty(example = "1", notes = "Vote id")
    private Long id;

    @ApiModelProperty(example = "1", notes = "Vote Voter id")
    private Long voterId;

    @ApiModelProperty(example = "1", notes = "Vote Candidate id")
    private Long candidateId;

    @ApiModelProperty(example = "true", notes = "Vote blankVote")
    private Boolean blankVote;

    @ApiModelProperty(example = "true", notes = "Vote nullVote")
    private Boolean nullVote;

    @ApiModelProperty(example = "teste", notes = "Vote Election")
    private ElectionOutput electionOutput;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Boolean getBlankVote() {
        return blankVote;
    }

    public void setBlankVote(Boolean blankVote) {
        this.blankVote = blankVote;
    }

    public Boolean getNullVote() {
        return nullVote;
    }

    public void setNullVote(Boolean nullVote) {
        this.nullVote = nullVote;
    }

    public ElectionOutput getElectionOutput() {
        return electionOutput;
    }

    public void setElectionOutput(ElectionOutput electionOutput) {
        this.electionOutput = electionOutput;
    }
}
