import SliderPage from '@/page/main/SliderPage';

function SwiperPage({ params }: { params: { id: number } }) {
	return <SliderPage id={params.id} isTrain />;
}

export default SwiperPage;
