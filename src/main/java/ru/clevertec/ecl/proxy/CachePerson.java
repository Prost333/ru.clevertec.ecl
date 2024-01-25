package ru.clevertec.ecl.proxy;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mapstruct.factory.Mappers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cache.Cache;
import ru.clevertec.ecl.cache.CacheFactory;
import ru.clevertec.ecl.dao.PersonDao;
import ru.clevertec.ecl.dto.personDto.PersonMapper;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exeption.EntityNotFoundException;


import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
@ConditionalOnBean(CacheFactory.class)
public class CachePerson {

    private final Cache<Object, Object> cache;

    private final PersonDao personRepository;

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    public CachePerson(CacheFactory factoryCache, PersonDao personRepository) {
        this.cache = factoryCache.getCacheAlgorithm();
        this.personRepository = personRepository;
    }

    @Around("@annotation(ru.clevertec.ecl.proxy.Cache) && execution(* ru.clevertec.ecl.service.imp.PersonServiceImp.findByUUID(..))")
    public Object cacheGet(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        UUID uuid = (UUID) args[0];
        if (cache.get(uuid) != null) {
            return cache.get(uuid);
        }
        PersonRes result;
        try {
            result = (PersonRes) joinPoint.proceed();
        } catch (ChangeSetPersister.NotFoundException exception) {
            throw new EntityNotFoundException("not found");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(uuid, result);
        return result;
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ecl.proxy.Cache) && execution(* ru.clevertec.ecl.service.imp.PersonServiceImp.save(..))", returning = "uuid")
    public void cacheCreate(UUID uuid) {

        Optional<Person> optionalPerson = personRepository.findPerson(uuid);
        optionalPerson.ifPresent(person -> cache.put(uuid, personMapper.toResponse(person)));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ecl.proxy.Cache) && execution(* ru.clevertec.ecl.service.imp.PersonServiceImp.update(..)) && args(uuid, personRequest)", argNames = "uuid, personRequest")
    public void cacheUpdate(UUID uuid, PersonReq personRequest) {

        Person person = personRepository.findPerson(uuid).orElseThrow(() -> new EntityNotFoundException("not found"));
        cache.put(uuid, personMapper.toResponse(person));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ecl.proxy.Cache) && execution(* ru.clevertec.ecl.service.imp.PersonServiceImp.delete(..)) && args(uuid)", argNames = "uuid")
    public void cacheDelete(UUID uuid) {

        cache.remove(uuid);
    }
}
