package br.edu.ulbra.election.election.client;


import br.edu.ulbra.election.election.output.v1.VoterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class VoterClientService {

    private final VoterClient voterClient;

    @Autowired
    public VoterClientService(VoterClient voterClient) {
        this.voterClient = voterClient;
    }

    public VoterOutput getById(Long id){
        return this.voterClient.getById(id);
    }

    public VoterOutput getToken(String token) {
        return voterClient.getToken(token);
    }

    @FeignClient(value="voter-client", url="${url.voter-service}")
    private interface VoterClient {

        @GetMapping("/v1/voter/{id}")
        VoterOutput getById(@PathVariable(name = "id") Long id);

        @GetMapping("check/{token}")
        VoterOutput getToken(@PathVariable(name="token") String token);
    }
}
