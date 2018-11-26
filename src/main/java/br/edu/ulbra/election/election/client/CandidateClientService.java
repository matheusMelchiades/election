package br.edu.ulbra.election.election.client;

import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class CandidateClientService {

    private final CandidateClient candidateClient;

    @Autowired
    public CandidateClientService(CandidateClient candidateClient) {
        this.candidateClient = candidateClient;
    }

    public List<CandidateOutput> getByNumberElection(Long numberElection) {
        return this.candidateClient.getByNumberElection(numberElection);
    }

    public CandidateOutput getById(Long candidateId) {
        return this.candidateClient.getById(candidateId);
    }

    @FeignClient(value="candidate-client", url="${url.candidate-service}")
    private interface CandidateClient {

        @GetMapping("/v1/candidate/numberElection/{numberElection}")
        List<CandidateOutput> getByNumberElection(@PathVariable(name = "numberElection") Long numberElection);

        @GetMapping("/v1/candidate/{candidateId}")
        CandidateOutput getById(@PathVariable(name = "candidateId")Long candidateId);
    }
}
