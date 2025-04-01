package dev.kgamble.perspective.browserresource.gateway;

import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.perspective.common.PerspectiveModule;
import com.inductiveautomation.perspective.common.api.BrowserResource;
import com.inductiveautomation.perspective.common.api.ComponentDescriptor;
import com.inductiveautomation.perspective.common.api.ComponentRegistry;
import com.inductiveautomation.perspective.gateway.api.PerspectiveContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BrowserResourceGatewayHook extends AbstractGatewayModuleHook {
    private static final Logger logger = LoggerFactory.getLogger(BrowserResourceGatewayHook.class);
    private GatewayContext context;
    private ComponentRegistry componentRegistry;
    private List<ComponentDescriptor> modifiedComponents;

    private static final Set<BrowserResource> BROWSER_RESOURCES = Set.of(
            new BrowserResource(
                    "example-bootstrap",
                    "/res/browser-resource-example/bootstrap.js",
                    BrowserResource.ResourceType.JS));

    @Override
    public void setup(GatewayContext context) {
        logger.info("Setting up Browser Resource Example module.");
        this.context = context;
    }

    @Override
    public void startup(LicenseState activationState) {
        logger.info("Starting up Browser Resource Example module.");
        PerspectiveContext perspectiveContext = PerspectiveContext.get(context);
        if (perspectiveContext != null) {
            this.componentRegistry = perspectiveContext.getComponentRegistry();
            if (this.componentRegistry != null) {
                logger.info("Injecting resources: {}", BROWSER_RESOURCES);
                this.modifiedComponents = new ArrayList<>();
                ComponentRegistryUtils.addResourcesTo(this.componentRegistry, BROWSER_RESOURCES,
                        component -> {
                            boolean matches = component.moduleId().equals(PerspectiveModule.MODULE_ID);
                            if (matches) {
                                modifiedComponents.add(component);
                            }
                            return matches;
                        });
                logger.debug("Tracked {} components for resource injection.", modifiedComponents.size());
            } else {
                logger.warn("ComponentRegistry is null, skipping resource injection.");
            }
        } else {
            logger.warn("PerspectiveContext is null, skipping resource injection.");
        }
    }

    @Override
    public void shutdown() {
        logger.info("Shutting down Browser Resource Example module.");
        if (this.componentRegistry != null && this.modifiedComponents != null) {
            logger.debug("Removing injected resources from {} tracked components...", modifiedComponents.size());
            ComponentRegistryUtils.removeResourcesFrom(this.componentRegistry, BROWSER_RESOURCES, modifiedComponents);
        }
    }

    @Override
    public boolean isFreeModule() {
        return true;
    }

    @Override
    public Optional<String> getMountedResourceFolder() {
        return Optional.of("static");
    }

    @Override
    public Optional<String> getMountPathAlias() {
        return Optional.of("browser-resource-example");
    }
}