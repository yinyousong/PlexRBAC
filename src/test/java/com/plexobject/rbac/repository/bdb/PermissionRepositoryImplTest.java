package com.plexobject.rbac.repository.bdb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.plexobject.rbac.repository.DomainRepository;
import com.plexobject.rbac.repository.PermissionRepository;
import com.plexobject.rbac.repository.RepositoryFactory;
import com.plexobject.rbac.domain.Domain;
import com.plexobject.rbac.domain.Permission;
import com.plexobject.rbac.domain.Subject;
import com.plexobject.rbac.utils.CurrentRequest;

public class PermissionRepositoryImplTest {
    private static final String USERNAME = "shahbhat";

    private static final String APPNAME = "appname";

    private static final String TEST_DB_DIR = "test_db_dir_perm";

    private static final Logger LOGGER = Logger
            .getLogger(PermissionRepositoryImplTest.class);
    private RepositoryFactory repositoryFactory;

    private DomainRepository appRepository;

    private PermissionRepository permissionRepository;

    @Before
    public void setUp() throws Exception {
        FileUtils.deleteDirectory(new File(TEST_DB_DIR));

        CurrentRequest.startRequest(APPNAME, USERNAME, "127.0.0.1");

        repositoryFactory = new RepositoryFactoryImpl(TEST_DB_DIR);

        appRepository = repositoryFactory.getDomainRepository();

        permissionRepository = repositoryFactory
                .getPermissionRepository(APPNAME);
    }

    @After
    public void tearDown() throws Exception {
        ((RepositoryFactoryImpl) repositoryFactory).close(APPNAME);
        FileUtils.deleteDirectory(new File(TEST_DB_DIR));
        CurrentRequest.endRequest();
    }

    @Test
    public void testGetAllDatabases() {
    }

    @Test
    public void testRemoveDatabase() {
    }

    @Test
    public void testCreateDatabase() {
    }

    @Test
    public void testFindAll() {
        repositoryFactory.getSubjectRepository(APPNAME).getOrCreateSubject(
                new Subject(USERNAME, "pass"));
        final Domain app = new Domain(APPNAME, "desc");
        appRepository.save(app);
        List<Permission> saved = new ArrayList<Permission>();

        for (int i = 0; i < 10; i++) {
            String operation = null;
            if (i == 0) {
                operation = "(read|write|update|delete)";
            } else if (i == 10 - 1) {
                operation = "*";
            } else if (i % 2 == 0) {
                operation = "read";
            } else {
                operation = "(read|write)";
            }
            Permission permission = new Permission(operation, "database",
                    "amount <= 500 && dept == 'sales' && time between 8:00am..5:00pm");
            LOGGER.debug("Saving " + permission);
            permissionRepository.save(permission);
            saved.add(permission);

        }

        Collection<Permission> all = permissionRepository.findAll(null, 10);
        int count = 0;
        for (Permission permission : all) {
            Assert.assertEquals("expected " + saved.get(count) + ", but was "
                    + permission, permission, saved.get(count));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("permission " + permission);
            }
            count++;
        }
        Assert.assertEquals(10, count);
    }

    @Test
    public void testFindById() {
    }

    @Test
    public void testRemove() {
    }

    @Test
    public void testSave() {
    }

}
