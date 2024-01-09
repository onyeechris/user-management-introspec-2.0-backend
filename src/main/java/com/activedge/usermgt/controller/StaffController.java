package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.ExcelGenerator;
import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.repository.ModuleRepository;
import com.activedge.usermgt.security.SecurityUtils;
import com.activedge.usermgt.service.StaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Staff.
 */
@RestController
@RequestMapping(StaffController.STAFFS)
@SecurityRequirement(name = "introspec-cas")
public class StaffController {

    private final Logger log = LoggerFactory.getLogger(StaffController.class);
    static final String STAFFS = "staffs";
    static final String STAFFS_PREFERENCE = "preference/{id}";
    static final String ENROLMENT_PREFERENCE = "enrol/{id}";
    private static final String STAFFS_DOWNLOAD = "download";
    private static final String STAFF_BY_ID = "{id}";
    private static final String STAFF_BY_USERNAME = "import/{username}";
    private static final String STAFF_BY_STAFF_ID = "import/staff/{id}";
    private static final String SEARCH_STAFF_BY_USERNAME_WILDCARD = "/searchStaff/{username}";

    static final String FILENAME = "UserList";

    @Autowired
    @Qualifier("db")
    private StaffService staffService;

    @Autowired
    @Qualifier("ldap")
    private StaffService ldapService;

    private final Environment env;

    private final ApplicationContext appCtx;

    private final ModuleRepository moduleRepository;

    public StaffController(ModuleRepository moduleRepository, Environment env, ApplicationContext appCtx) {
        this.moduleRepository = moduleRepository;
        this.env = env;
        this.appCtx = appCtx;
    }

    /**
     * POST  /staff : Create a new staff.
     *
     * @param staffDTO the staffDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new staffDTO, or with status 400 (Bad Request) if the staff has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping(produces = "application/json")
    public ResponseEntity<StaffDTO> createStaff(@Valid @RequestBody NewStaffDTO staffDTO, Errors errors) throws Exception {
        log.info("---REST request to save a {} : {}, token: {}", STAFFS, staffDTO, SecurityUtils.getCurrentUserLogin());

        if (errors.hasErrors()) {
            log.error("Error in creating new user detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }

        staffDTO.setId(null);
        StaffDTO result = staffService.save(staffDTO);


        return ResponseEntity.created(new URI("/api/"+ STAFFS +"/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(STAFFS, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /staff : Updates an existing staff.
     *
     * @param staffDTO the staffDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated staffDTO,
     * or with status 400 (Bad Request) if the staffDTO is not valid,
     * or with status 500 (Internal Server Error) if the staffDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping
    public ResponseEntity<StaffDTO> updateStaff(@Valid @RequestBody StaffDTO staffDTO, Errors errors) throws Exception {
        log.debug("REST request to update {} : {}", STAFFS, staffDTO);

        if (errors.hasErrors() || staffDTO.getId() == null) {
            log.error("Error in creating new user detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }

        StaffDTO result = staffService.save(staffDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(STAFFS, staffDTO.getId().toString()))
            .body(result);
    }

//    @PutMapping(ENROLMENT_PREFERENCE)
//    public ResponseEntity<StaffDTO> updateStaffPreference(@PathVariable String id, @RequestBody StaffDTO staffDTO, Errors errors) throws Exception {
//        log.debug("REST request to update enrolment preference {} : {}", ENROLMENT_PREFERENCE, id);
//        if (errors.hasErrors() || id == null) {
//            log.error("Error in updating user preference detected...\n{}", errors.getAllErrors());
//            throw new ValidationException(errors.getAllErrors().stream()
//                    .map(x -> x.getDefaultMessage())
//                    .collect(Collectors.joining(",")));
//        }
//        StaffDTO result = staffService.savePreference(id, staffDTO);
//        return ResponseEntity.ok()
//                .headers(HeaderUtil.createEntityUpdateAlert(ENROLMENT_PREFERENCE, id))
//                .body(result);
//    }

//    @PutMapping(STAFFS_PREFERENCE)
//    public ResponseEntity<String> updateAllStaffPreference(@PathVariable String id,@RequestBody StaffDTO staffDTO, Errors errors) throws Exception {
//        log.debug("REST request to update preference {} : {}", STAFFS_PREFERENCE, id);
//        if (errors.hasErrors() || id == null) {
//            log.error("Error in updating user preference detected...\n{}", errors.getAllErrors());
//            throw new ValidationException(errors.getAllErrors().stream()
//                    .map(x -> x.getDefaultMessage())
//                    .collect(Collectors.joining(",")));
//        }
//        Integer result = staffService.enable2faForAllStaff(id, staffDTO);
//        return ResponseEntity.ok()
//                .headers(HeaderUtil.createEntityUpdateAlert(STAFFS_PREFERENCE, id))
//                .body(String.format("%s staff preferences updated", result));
//    }

    /**
     * GET  /staff : get all the staff.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of staff in body
     */
    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllStaff(@RequestHeader(value = "Module", required = true) String mdl, Pageable pageable) throws ServletRequestBindingException {
        log.debug("REST request to get a page of "+ STAFFS);

        Page<StaffDTO> page = null;

        Optional<Module> module = this.moduleRepository.findById(mdl);

        if(!module.isPresent()) {
//            page = staffService.findAllBy(module, pageable);
        } else {
            page = staffService.findAll(pageable);
        }

        return new ResponseEntity<>(new ResponseWrapper(page), HttpStatus.OK);
    }

    /**
     * GET  /staff : get all the staff.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of staff in body
     */
    @GetMapping(STAFFS_DOWNLOAD)
    public ResponseEntity<InputStreamResource> downloadAllStaff(@RequestHeader(value = "Module", required = true) String mdl, Pageable pageable) throws IOException {
        ByteArrayInputStream in = null;

        Optional<Module> module = this.moduleRepository.findById(mdl);

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String currentDateTime = dateFormatter.format(new Date());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Content-Disposition", "attachment; filename=" + FILENAME + currentDateTime + ".xlsx");

        if(!module.isPresent()) {
        } else {
            in = ExcelGenerator.generateUserList(staffService.findAll(pageable));
        }

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /**
     * GET  /staff/:id : get the "id" staff.
     *
     * @param id the id of the staffDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staffDTO, or with status 404 (Not Found)
     */
    @GetMapping(STAFF_BY_ID)
    public ResponseEntity<StaffDTO> getStaff(@PathVariable String id) throws Exception {
        log.debug("REST request to get {} : {}", STAFFS, id);
        Optional<StaffDTO> staffDTO = staffService.findOne(id);

        if (!staffDTO.isPresent()) {
            throw new ValidationException("No "+ STAFFS +" was found for id " + id);
        }

        return new ResponseEntity<>(staffDTO.get(), HttpStatus.OK);

    }

    /**
     * GET  /staff/import/:username : import staff with username from LDAP.
     *
     * @param username the username of the staffDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staffDTO, or with status 404 (Not Found)
     */
    @GetMapping(STAFF_BY_USERNAME)
    public ResponseEntity<StaffDTO> importStaff(@PathVariable String username) throws Exception {
        log.debug("REST request to import {} : {}", STAFFS, username);

        StaffService service = appCtx.getBean(env.getProperty("introspecsso.backend"), StaffService.class);

        Optional<StaffDTO> staffDTO = service.findByUsername(username);

        if (!staffDTO.isPresent()) {
            throw new ValidationException("No "+ STAFFS +" was found for username " + username);
        }
        return new ResponseEntity<>(staffDTO.get(), HttpStatus.OK);
    }
    @GetMapping(SEARCH_STAFF_BY_USERNAME_WILDCARD)
    public ResponseEntity<List<StaffDTO>> searchStaff(@PathVariable String username) {
        log.debug("REST request to search for staff by username: {}", username);
        StaffService service = appCtx.getBean(env.getProperty("introspecsso.backend"), StaffService.class);
        List<StaffDTO> staffDTOs = service.wildcardSearch(username);
        if (staffDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if no staff is found
        }
        return new ResponseEntity<>(staffDTOs, HttpStatus.OK);
    }

    /**
     * DELETE  /staff/:id : delete the "id" staff.
     *
     * @param id the id of the staffDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping(STAFF_BY_ID)
    public ResponseEntity<Void> deleteStaff(@PathVariable String id) {
        log.debug("REST request to delete {} : {}", STAFFS, id);
        staffService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(STAFFS, id.toString())).build();
    }
    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email){
       return new ResponseEntity<>(staffService.forgotPassword(email),HttpStatus.OK);
    }
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email,@RequestHeader String newPassword){
        return new ResponseEntity<>(staffService.resetPassword(email,newPassword),HttpStatus.OK);

    }
    @PostMapping("/download-Columns")
    public ResponseEntity<byte[]> downloadCSV(@RequestBody List<List<String>> columns) {
        // Create CSV content from received columns
        StringBuilder csvContent = new StringBuilder();

        // Assuming all columns have the same length (rows)
        int numRows = columns.get(0).size();

        for (int i = 0; i < numRows; i++) {
            for (List<String> column : columns) {
                csvContent.append(column.get(i)).append(",");
            }
            csvContent.deleteCharAt(csvContent.length() - 1); // Remove the last comma
            csvContent.append("\n"); // New line for each row
        }
        byte[] bytes = csvContent.toString().getBytes();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        responseHeaders.setContentDispositionFormData("attachment", "Introspec.csv");
        responseHeaders.setContentLength(bytes.length);
        return ResponseEntity.ok().headers(responseHeaders).body(bytes);
    }

    }
