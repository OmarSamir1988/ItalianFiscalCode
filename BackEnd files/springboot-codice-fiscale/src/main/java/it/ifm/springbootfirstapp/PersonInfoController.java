package it.ifm.springbootfirstapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/person")
@CrossOrigin(origins = "*")
public class PersonInfoController {

    private final PersonService personService;

    @Autowired
    public PersonInfoController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/generate-code")
    public CF generateCode(@RequestBody PersonInfo personInfo) {
        String code = personService.generateCode(personInfo);
        Integer Zcode = personService.isPlaceCodeZ(code);

        if (code == null) {
            return null;
        }
        CF cf = new CF();
        cf.setC(code);
        cf.setZ(Zcode);  // Assuming CF.z is a string
        return cf;
    }
}

