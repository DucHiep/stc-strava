package application.service;


import application.repository.RunRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RunService {
    @Autowired
    private RunRepositoy runRepositoy;

}
