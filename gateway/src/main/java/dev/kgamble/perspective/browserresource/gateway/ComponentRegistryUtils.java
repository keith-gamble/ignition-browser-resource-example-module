package dev.kgamble.perspective.browserresource.gateway;

import com.inductiveautomation.perspective.common.api.BrowserResource;
import com.inductiveautomation.perspective.common.api.ComponentDescriptor;
import com.inductiveautomation.perspective.common.api.ComponentDescriptorImpl;
import com.inductiveautomation.perspective.common.api.ComponentRegistry;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentRegistryUtils {
    private static final Logger logger = LoggerFactory.getLogger(ComponentRegistryUtils.class);

    public static void addResourcesTo(ComponentRegistry registry, Set<BrowserResource> resources,
            Predicate<ComponentDescriptor> predicate) {
        for (ComponentDescriptor component : registry.get().values()) {
            if (predicate.test(component)) {
                for (BrowserResource resource : resources) {
                    logger.debug("Adding resource {} to component {}", resource, component);
                    addBrowserResource(component, resource);
                }
            }
        }
    }

    public static void removeResourcesFrom(ComponentRegistry registry, Set<BrowserResource> resources,
            List<ComponentDescriptor> components) {
        for (ComponentDescriptor component : components) {
            for (BrowserResource resource : resources) {
                logger.debug("Removing resource {} from component {}", resource, component);
                removeBrowserResource(component, resource);
            }
        }
    }

    private static void addBrowserResource(ComponentDescriptor descriptor, BrowserResource resource) {
        try {
            Set<BrowserResource> browserResources = new HashSet<>(descriptor.browserResources());
            browserResources.add(resource);
            Field field = ComponentDescriptorImpl.class.getDeclaredField("browserResources");
            field.setAccessible(true);
            field.set(descriptor, browserResources);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Failed to add browser resource {} to descriptor {}", resource, descriptor, e);
            throw new RuntimeException("Failed to add browser resource", e);
        }
    }

    private static void removeBrowserResource(ComponentDescriptor descriptor, BrowserResource resource) {
        try {
            Set<BrowserResource> browserResources = new HashSet<>(descriptor.browserResources());
            browserResources.remove(resource);

            Field field = ComponentDescriptorImpl.class.getDeclaredField("browserResources");
            field.setAccessible(true);
            field.set(descriptor, browserResources);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Failed to remove browser resource {} from descriptor {}", resource, descriptor, e);
            throw new RuntimeException("Failed to remove browser resource", e);
        }
    }
}