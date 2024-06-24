package com.myScm.scm.serviceImpl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.myScm.scm.errorHandler.ResourceNotFoundException;
import com.myScm.scm.errorHandler.UnExpectedException;
import com.myScm.scm.helper.AppConstants;
import com.myScm.scm.services.cloudnaryImageService;

@Service
public class CloudnairyServiceImpl implements cloudnaryImageService {

    private Cloudinary cloudinary;

    public CloudnairyServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String uploadOnCloud(MultipartFile file, String publicId) {

        @SuppressWarnings("unused")
        Map map = null;
        try {

            map = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", publicId));

            return getUrlOfImage(publicId);

        } catch (IOException io) {
            throw new UnExpectedException(
                    "we are not able to upload the image of the contact so please try again later some time!!");
        }

    }

    @Override
    public String getUrlOfImage(String publicId) {

        return cloudinary
                .url().transformation(
                        new Transformation<>()
                                .width(AppConstants.CONTACT_IMAGE_WIDTH)
                                .height(AppConstants.CONTACT_IMAGE_HEIGTH)
                                .crop(AppConstants.CONTACT_IMAGE_CROP))
                .generate(publicId);

    }

    public boolean deleteImage(String publicId) {

        try {
            this.cloudinary.uploader().destroy(publicId, ObjectUtils.asMap());
            return true;
        } catch (IOException e) {
            throw new ResourceNotFoundException(
                    "we are not able to find the resources for this contact so please try again later some time!! ");
        }

    }

}
