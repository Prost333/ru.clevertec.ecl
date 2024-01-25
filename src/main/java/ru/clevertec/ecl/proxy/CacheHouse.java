package ru.clevertec.ecl.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cache.Cache;
import ru.clevertec.ecl.cache.CacheFactory;
import ru.clevertec.ecl.dao.HouseDao;
import ru.clevertec.ecl.dto.HouseDto.HouseMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.exeption.EntityNotFoundException;


import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
@ConditionalOnBean(CacheFactory.class)
public class CacheHouse {
    private final Cache<Object, Object> cache;

    private final HouseDao houseRepository;

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);
    ;

    public CacheHouse(CacheFactory factoryCache, HouseDao houseRepository) {
        this.cache = factoryCache.getCacheAlgorithm();
        this.houseRepository = houseRepository;
    }

    @Around("@annotation(ru.clevertec.ecl.proxy.Cache) && execution(* ru.clevertec.ecl.service.imp.HouseServiceImp.findByUUID(..))")
    public Object cacheGet(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        UUID uuid = (UUID) args[0];
        if (cache.get(uuid) != null) {
            return cache.get(uuid);
        }
        HouseRes result;
        try {
            result = (HouseRes) joinPoint.proceed();
        } catch (EntityNotFoundException exception) {
            throw exception;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(uuid, result);
        return result;
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ecl.proxy.Cache) && execution(* ru.clevertec.ecl.service.imp.HouseServiceImp.save(..))", returning = "uuid")
    public void cacheCreate(UUID uuid) {

        Optional<House> optionalHouse = Optional.ofNullable(houseRepository.findByUUID(uuid));
        optionalHouse.ifPresent(house -> cache.put(uuid, houseMapper.toResponse(house)));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ecl.proxy.Cache) && execution(* ru.clevertec.ecl.service.imp.HouseServiceImp.update(..)) && args(uuid, houseRequest)", argNames = "uuid, houseRequest")
    public void cacheUpdate(UUID uuid, HouseReq houseRequest) throws EntityNotFoundException {

        House house = houseRepository.findByUUID(uuid);
        cache.put(uuid, houseMapper.toResponse(house));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ecl.proxy.Cache) && execution(* ru.clevertec.ecl.service.imp.HouseServiceImp.delete(..)) && args(uuid)", argNames = "uuid")
    public void cacheDelete(UUID uuid) {

        cache.remove(uuid);
    }
}

