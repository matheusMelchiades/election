package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.client.CandidateClientService;
import br.edu.ulbra.election.election.client.VoterClientService;
import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.*;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private final ModelMapper modelMapper;

    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;

    private final CandidateClientService candidateClientService;
    private final VoterClientService voterClientService;

    private final String MESSAGE_VOTER_INVALID = "Invalid Voter";
    private final String MESSAGE_VOTER_NOT_LOGGED = "Invalid, Voter not logged";

    @Autowired
    public VoteService(ModelMapper modelMapper, VoteRepository voteRepository, ElectionRepository electionRepository, CandidateClientService candidateClientService, VoterClientService voterClientService){
        this.modelMapper = modelMapper;
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.candidateClientService = candidateClientService;
        this.voterClientService = voterClientService;
    }

    /*************     TEST  ****************************************/
    public List<VoteOutput> getAll() {
        List<Vote> voteList = (List<Vote>) voteRepository.findAll();
        return voteList.stream().map(this::toVoteOutput).collect(Collectors.toList());
    }

    public VoteOutput toVoteOutput(Vote vote) {
        VoteOutput voteOutput = modelMapper.map(vote, VoteOutput.class);

        Election election = electionRepository.findById(vote.getElection().getId()).orElse(null);


        voteOutput.setElectionOutput(modelMapper.map(election, ElectionOutput.class));

        return voteOutput;
    }

    /***************************************************************/

    public GenericOutput electionVote(String token, VoteInput voteInput){
        validateToken(token);

        Election election = validateInput(voteInput.getElectionId(), voteInput);

        Vote vote = new Vote();
        vote.setElection(election);
        vote.setVoterId(voteInput.getVoterId());

        if (voteInput.getCandidateNumber() == null){
            vote.setBlankVote(true);
            vote.setNullVote(false);
        } else {
            vote.setBlankVote(false);
            vote.setNullVote(true);

            CandidateOutput candidateOutput = validateInput(voteInput);

            if(candidateOutput.getId() != null) {
                vote.setNullVote(false);
                vote.setCandidateId(candidateOutput.getId());
            }
        }

        voteRepository.save(vote);

        return new GenericOutput("OK");
    }

    public GenericOutput multiple(String token, List<VoteInput> voteInputList){
        for (VoteInput voteInput : voteInputList){
            this.electionVote(token, voteInput);
        }
        return new GenericOutput("OK");
    }

    public void validateToken(String token) {
        try {
            voterClientService.getToken(token);
        } catch (FeignException e) {
            if(e.status() == 500) {
                throw new GenericOutputException(MESSAGE_VOTER_NOT_LOGGED);
            }
        }

    }

    public Election validateInput(Long electionId, VoteInput voteInput) {
        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null) {
            throw new GenericOutputException("Invalid Election");
        }
        if (voteInput.getVoterId() == null) {
            throw new GenericOutputException(MESSAGE_VOTER_INVALID);
        }

        try{
            VoterOutput voterOutput = voterClientService.getById(voteInput.getVoterId());

            if(voterOutput.getId() != voteInput.getVoterId()){
                throw new GenericOutputException(MESSAGE_VOTER_INVALID);
            }
        } catch (FeignException e){
            if(e.status() == 500) {
                throw new GenericOutputException(MESSAGE_VOTER_INVALID + ", this voter not found");
            }
        }

        if (voteRepository.findByVoterIdAndElection_Id(voteInput.getVoterId(), electionId).orElse(null) != null) {
            throw new GenericOutputException(MESSAGE_VOTER_INVALID + ", this voter already voted");
        }

        return election;
    }

    public CandidateOutput validateInput(VoteInput voteInput) {
        List<CandidateOutput> candidateOutputList = null;
        CandidateOutput candidateOutput = new CandidateOutput();

        try{
            candidateOutputList = candidateClientService.getByNumberElection(voteInput.getCandidateNumber());

        } catch (FeignException e) {
            if(e.status() == 500) {
                System.out.println("Not Found Candidate");
            }
        }

        for (CandidateOutput candidate : candidateOutputList) {
            if(candidate.getElectionOutput().getId() == voteInput.getElectionId()) {
                candidateOutput = candidate;
            }
        }
        return candidateOutput;
    }


}
