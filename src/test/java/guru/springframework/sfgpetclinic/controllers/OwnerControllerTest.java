package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    public static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    Model model;
    @Mock
    OwnerService service;

    @InjectMocks
    OwnerController controller;

    @Mock
    BindingResult bindingResult;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp(){
        given(service.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .willAnswer(invocation -> {
                    List<Owner> ownerList = new ArrayList<>();
                    String name = invocation.getArgument(0);

                    if(name.equals("%Buck%")){
                        ownerList.add(new Owner(1L, "Joe", "Buck"));
                        return  ownerList;
                    }else if(name.equals("%DontFindMe%")){
                        return ownerList;
                    }else if(name.equals("%FindMe%")){
                        ownerList.add(new Owner(1L, "Joe", "Buck"));
                        ownerList.add(new Owner(2L, "Ricky", "Buck"));
                        return  ownerList;
                    }

                    throw new RuntimeException("Invalid Argument");
                });
    }
    @Test
    void processFindFormWildCardFound() {
        //given
        Owner owner = new Owner(1L, "Joe", "FindMe");
        InOrder inOrder = inOrder(service, model);
        //when
        String viewName = controller.processFindForm(owner, bindingResult, model);

        //then
        assertThat("%FindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);

        //inorder asserts
        //It's like a regular verify but we are specifying de order in which these mocks should be invoked
        inOrder.verify(service).findAllByLastNameLike(anyString());
        inOrder.verify(model, times(1)).addAttribute(anyString(), anyList());
        verifyNoMoreInteractions(model);
    }

    @Test
    void processFndFormWildCardStringAnnotation() {
        //given
        Owner owner = new Owner(1L, "Joe", "Buck");

        //when
        String viewName =  controller.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
        verifyZeroInteractions(model);
    }

    @Test
    void processFndFormWildCardNotFound() {
        //given
        Owner owner = new Owner(1L, "Joe", "DontFindMe");

        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
        verifyZeroInteractions(model);
    }

 /* @Test
    void processCreationFormHasErrors() {
        //given
        Owner owner = new Owner(1L, "Jim", "Bob");
        given(bindingResult.hasErrors()).willReturn(true);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);
        //then

        assertThat(viewName).isEqualTo(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
    }

    @Test
    void processCreationFormNoErrors() {
        //given
        Owner owner = new Owner(5L, "Jim", "Buck");
        given(bindingResult.hasErrors()).willReturn(false);
        given(service.save(any(Owner.class))).willReturn(owner);
        //when
        String viewName = controller.processCreationForm(owner, bindingResult);
        //then

        assertThat(viewName).isEqualTo(REDIRECT_OWNERS_5);
    }*/
}